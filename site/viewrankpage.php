<?php
// +++++++++++ Просмотр рейтинга пользователей ++++++++++++++++++++++++++++++++++++++++

// Выходим, если файл был запрошен напрямую, а не через include
if (!isset($MyPHPScript)) return;

?>
<script language = "JavaScript">

        


	// Посмотреть профиль пользователя
	function ViewUserInfo(userid)
	{
		document.RankUsersForm.UserId.value = userid;
		document.RankUsersForm.action.value = "UserInfo";
		document.RankUsersForm.submit();
	}
	
	
	
	// Посмотреть команду
	function ViewTeamInfo(teamid)
	{
		document.RankUsersForm.TeamId.value = teamid;
		document.RankUsersForm.action.value = "TeamInfo";
		document.RankUsersForm.submit();
	}



</script>

<?php


               $TabIndex = 0;
	       
           	print('<form  name = "RankUsersForm"  action = "'.$MyPHPScript.'" method = "post">'."\r\n");
                print('<input type = "hidden" name = "action" value = "">'."\r\n");
	        print('<input type = "hidden" name = "UserId" value = "0">'."\n");
	        print('<input type = "hidden" name = "TeamId" value = "0">'."\n");
		print('<input type = "hidden" name = "sessionid" value = "'.$SessionId.'">'."\n");
		
		if (!isset($_POST['ShowAllRaids'])) {
		  $ShowAllRaids = '';
		} else {
		  $ShowAllRaids = $_POST['ShowAllRaids'];
		}
		
		
		$DisabledText = '';
		
	        print('Отображать все марш-броски (долгая загрузка) <input type="checkbox"  autocomplete = "off" name="ShowAllRaids" '.(($ShowAllRaids == 'on') ? 'checked="checked"' : '').' tabindex = "'.(++$TabIndex).'" '.$DisabledText.'
	        title = "Отображать все марш-броски (долгая загрузка)" onclick = "javacript:document.RankUsersForm.action.value = \'ViewRankPage\';document.RankUsersForm.submit();" />'."\r\n");


	        print('</form>'."\r\n");

               print('</br>'."\r\n");
 	       print('<div style = "margin-top: 15px;" align = "left">Рейтинг по версии slazav: по всем ММБ суммируется отношение времени лидера к времени участника.
	               </br>Для марш-бросков с несколькими дистанциями это отношение дополнительно умножается на отношение  длины текущей дистанции к максимальной  из длин дистанций.
		        Рейтинг участника марш-браска не рассчитывается в следующих случаях: 1) команда вне зачёта; 2) команда не финишировала; 3) участник сошёл с дистанции. 
                        Для марш-бросков до 2012 года сход участников не отражён в данных - можно сообщать о неточностях на общий адрес или в сообщество (ЖЖ) 			
		       </div>'."\r\n");
               print('</br>'."\r\n");


          // Возможно здесь нужно вызвать пересчёт рейтинга по всем ММБ и это не долго
	  // RecalcTeamUsersRank(0);
	  

	  $sql = "  select tu.user_id, CASE WHEN COALESCE(u.user_noshow, 0) = 1 THEN '".$Anonimus."' ELSE u.user_name END as user_name,  SUM(tu.teamuser_rank) as userrank, 
	                   COUNT(tu.teamuser_id) as userrankcount, 
			   0 as distance_id, '&nbsp;' as distance_name,  'Итог' as raid_name,
			   '&nbsp;' as team_num, '&nbsp;' as team_name
	            from  TeamUsers tu
		          inner join Users u
			  on u.user_id = tu.user_id
		          inner join Teams t
			  on t.team_id = tu.team_id
		          inner join Distances d
			  on t.distance_id = d.distance_id
		    where d.distance_hide = 0 
			  and t.team_hide = 0 
		          and  COALESCE(t.team_outofrange, 0) = 0
		          and  COALESCE(t.team_result, 0) > 0
			  and  COALESCE(tu.teamuser_rank, 0) > 0
		    group by tu.user_id
		    order by userrank DESC 
		  "; 


/*

		    ) a
		    inner join 
		    (
                    select tu.user_id, u.user_name,  tu.teamuser_rank as userrank, null as  userrankcount,
		           d.distance_id, d.distance_name, r.raid_name,
			   t.team_num, t.team_name
	            from  Raids r
		          inner join Distances d
			  on r.raid_id = d.distance_id 
		          left outer join Teams t
			  on t.distance_id = d.distance_id
			     and t.team_hide = 0 
		             and  COALESCE(t.team_outofrange, 0) = 0
		             and  COALESCE(t.team_result, 0) > 0
                          left outer join TeamUsers tu
			  on t.team_id = tu.team_id			   
		          left outer join Users u
			  on u.user_id = tu.user_id
		    where d.distance_hide = 0 
		    ) b
		    on a.user_id = b.user_id

*/	
	  	//echo 'sql '.$sql;
                $Result = MySqlQuery($sql);
	

		$tdstyle = 'padding: 5px 0px 2px 5px;';		
              //  $tdstyle = '';		
                $thstyle = 'padding: 5px 0px 0px 5px;';		
               // $thstyle = '';		

               if ($ShowAllRaids) 
	       {
		$sqlRaids = "select r.raid_id, r.raid_name, d.distance_name, d.distance_id from Raids r 
					     inner join Distances d on r.raid_id = d.raid_id and d.distance_hide = 0
                             order by r.raid_id  desc, d.distance_id desc ";
		$ResultRaids = MySqlQuery($sqlRaids);
                $RowCount = mysql_num_rows($ResultRaids);
                $TableWidth =  $RowCount*100 + 550;
               } else {
	         $TableWidth = 550;
	       }
		print('<table border = "1" width = "'.$TableWidth.'" cellpadding = "0" cellspacing = "0" style = "font-size: 80%">'."\r\n");  

		print('<tr class = "gray">
 	                 <td width = "100" style = "'.$thstyle.'">N строки</td>
 	                 <td width = "350" style = "'.$thstyle.'">Пользователь</td>
		         <td width = "100" align = "center" style = "'.$thstyle.'">Рейтинг</td>'."\r\n");

               if ($ShowAllRaids) 
	       {

	     // Показываем  список ММБ
		while ($RowRaids = mysql_fetch_assoc($ResultRaids))
		{
                         print('<td width = "100"  style = "'.$thstyle.'">'.$RowRaids['raid_name'].' '.$RowRaids['distance_name'].'</td>'."\r\n");
		}
		mysql_free_result($ResultRaids);
               }
	       
		print('</tr>'."\r\n");
 	
	
	

	
	        $LineNum = 0;	
	        // Сканируем команды
		while ($Row = mysql_fetch_assoc($Result))
		{
	 	//   print('<tr class = "'.$TrClass.'">'."\r\n");
                     $LineNum++;

                     print('<tr>'."\r\n");
		     print('<td align = "left" style = "'.$tdstyle.'">'.$LineNum.'</td>'."\r\n");
		     print('<td align = "left" style = "'.$tdstyle.'"><a href = "javascript:ViewUserInfo('.$Row['user_id'].');">'.$Row['user_name'].'</a></td>'."\r\n");
		     print('<td align = "center" style = "'.$tdstyle.'">'.$Row['userrank'].'</td>'."\r\n");

               if ($ShowAllRaids) 
	       {

                  // Показываем  список ММБ
		$sqlRaids = "select r.raid_id, d.distance_id, a.team_name, a.team_id, a.team_outofrange,
		                    a.level_name, a.levelpoint_name, a.teamuser_rank, a.level_id, a.levelpoint_id
		        from Raids r 
			     inner join Distances d on r.raid_id = d.raid_id and d.distance_hide = 0
			     left outer join (select t.distance_id, t.team_name, t.team_id, t.team_outofrange,
		                                     l.level_name, lp.levelpoint_name, tu.teamuser_rank,
						     l.level_id, lp.levelpoint_id 
			                      from Teams t 
			                           inner join TeamUsers tu  on t.team_id = tu.team_id and tu.teamuser_hide = 0 and tu.user_id = ".$Row['user_id']."
						   left outer join Levels l on tu.level_id = l.level_id
			                           left outer join LevelPoints lp on tu.levelpoint_id = lp.levelpoint_id
					      where   t.team_hide = 0) a
                             on d.distance_id = a.distance_id					       	   
		        order by r.raid_id  desc,  d.distance_id desc";
		$ResultRaids = MySqlQuery($sqlRaids);
		while ($RowRaids = mysql_fetch_assoc($ResultRaids))
		{
                         
			 if (!empty($RowRaids['team_name']))
			 {
               			$TeamPlace = GetTeamPlace($RowRaids['team_id']);
				$LevelId = $RowRaids['level_id'];
				$LevelPointId = $RowRaids['levelpoint_id'];
				$TeamPlaceResult = "";
				// Есть место команды и нет схода участника
				if ($TeamPlace > 0 and $LevelId == 0 and $LevelPointId == 0) $TeamPlaceResult = ", место ".$TeamPlace."";

				$TeamUserOff = "";
				// Есть место команды, но сход участника
			//	if ($TeamPlace > 0 and $LevelId > 0) $TeamUserOff = ", сход на этапе <b>".$RowRaids['level_name']."</b>";
			//	if ($TeamPlace > 0 and $LevelPointId > 0) $TeamUserOff = ", не явка в точку <b>".$RowRaids['levelpoint_name']."</b>";


			   $TeamString = '<a href = "javascript:ViewTeamInfo('.$RowRaids['team_id'].');">'.$RowRaids['team_name'].'</a></br>'.$RowRaids['teamuser_rank'].$TeamPlaceResult.$TeamUserOff;
			 } else {
				$TeamString = '&nbsp;';
			 }	
			 
                         print('<td align = "left" style = "'.$tdstyle.'">'. $TeamString.'</td>'."\r\n");
		}
		mysql_free_result($ResultRaids);
               }

                     print('</tr>'."\r\n");
			
		}	
                // Конец циклда по журанлу объединений

		mysql_free_result($Result);
		
		print('</table>'."\r\n");
		
  	  
?>
		

		</br>


