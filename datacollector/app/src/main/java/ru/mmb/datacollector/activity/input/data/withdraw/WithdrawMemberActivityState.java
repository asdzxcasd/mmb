package ru.mmb.datacollector.activity.input.data.withdraw;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.mmb.datacollector.R;
import ru.mmb.datacollector.activity.ActivityStateWithTeamAndScanPoint;
import ru.mmb.datacollector.activity.input.data.withdraw.list.TeamMemberRecord;
import ru.mmb.datacollector.db.DatacollectorDB;
import ru.mmb.datacollector.model.Participant;
import ru.mmb.datacollector.model.RawTeamLevelDismiss;
import ru.mmb.datacollector.model.Team;
import ru.mmb.datacollector.model.history.DataStorage;
import ru.mmb.datacollector.model.registry.TeamsRegistry;
import ru.mmb.datacollector.model.registry.UsersRegistry;

import static ru.mmb.datacollector.activity.Constants.KEY_CURRENT_INPUT_WITHDRAWN_CHECKED;

public class WithdrawMemberActivityState extends ActivityStateWithTeamAndScanPoint {
    private final List<Participant> prevWithdrawnMembers = new ArrayList<Participant>();
    private final List<Participant> currWithdrawnMembers = new ArrayList<Participant>();

    public WithdrawMemberActivityState() {
        super("input.withdraw");
    }

    public List<Participant> getAllWithdrawnMembers() {
        List<Participant> result = new ArrayList<Participant>();
        result.addAll(prevWithdrawnMembers);
        result.addAll(currWithdrawnMembers);
        return result;
    }

    public boolean isPrevWithdrawn(Participant member) {
        return prevWithdrawnMembers.contains(member);
    }

    public boolean isCurrWithdrawn(Participant member) {
        return currWithdrawnMembers.contains(member);
    }

    public void setCurrWithdrawn(Participant member, boolean withdraw) {
        if (withdraw) {
            if (!currWithdrawnMembers.contains(member)) {
                currWithdrawnMembers.add(member);
                Collections.sort(currWithdrawnMembers);
                fireStateChanged();
            }
        } else {
            if (currWithdrawnMembers.contains(member)) {
                currWithdrawnMembers.remove(member);
                fireStateChanged();
            }
        }
    }

    @Override
    protected void update(boolean fromSavedBundle) {
        super.update(fromSavedBundle);
        updatePrevWithdrawnMembers();
    }

    private void updatePrevWithdrawnMembers() {
        if (getCurrentScanPoint() != null && getCurrentTeam() != null) {
            prevWithdrawnMembers.clear();
            prevWithdrawnMembers.addAll(DatacollectorDB.getConnectedInstance().getDismissedMembers(getCurrentScanPoint(), getCurrentTeam()));
        }
    }

    public CharSequence getResultText(Activity context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getResources().getString(R.string.input_withdraw_res_members));
        sb.append("\n");
        if (currWithdrawnMembers.size() == 0) {
            sb.append(context.getResources().getString(R.string.input_withdraw_res_no_members));
        } else {
            for (int i = 0; i < currWithdrawnMembers.size(); i++) {
                if (i > 0) sb.append("; ");
                Participant member = currWithdrawnMembers.get(i);
                sb.append(member.getUserName());
            }
        }
        return sb.toString();
    }

    public List<TeamMemberRecord> getMemberRecords() {
        List<TeamMemberRecord> result = new ArrayList<TeamMemberRecord>();
        for (Participant member : getCurrentTeam().getMembers()) {
            result.add(new TeamMemberRecord(member, isPrevWithdrawn(member)));
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public void save(Bundle savedInstanceState) {
        super.save(savedInstanceState);
        savedInstanceState.putSerializable(KEY_CURRENT_INPUT_WITHDRAWN_CHECKED, (Serializable) getCurrWithdrawnIds());
    }

    private List<Integer> getCurrWithdrawnIds() {
        List<Integer> result = new ArrayList<Integer>();
        for (Participant member : currWithdrawnMembers) {
            result.add(member.getUserId());
        }
        return result;
    }

    @Override
    public void load(Bundle savedInstanceState) {
        super.load(savedInstanceState);
        currWithdrawnMembers.clear();
        if (savedInstanceState.containsKey(KEY_CURRENT_INPUT_WITHDRAWN_CHECKED)) {
            @SuppressWarnings("unchecked")
            List<Integer> idList =
                    (List<Integer>) savedInstanceState.getSerializable(KEY_CURRENT_INPUT_WITHDRAWN_CHECKED);
            loadCurrWithdrawnMembers(idList);
        }
    }

    private void loadCurrWithdrawnMembers(List<Integer> idList) {
        if (getCurrentTeam() != null) {
            int teamId = getCurrentTeam().getTeamId();
            Team team = TeamsRegistry.getInstance().getTeamById(teamId);
            if (team != null) {
                for (Integer participantId : idList) {
                    Participant member = team.getMember(participantId);
                    if (member != null && !currWithdrawnMembers.contains(member))
                        currWithdrawnMembers.add(member);
                }
            }
        }
    }

    public void saveCurrWithdrawnToDB(Date recordDateTime) {
        DatacollectorDB.getConnectedInstance().saveDismissedMembers(getCurrentScanPoint(), getCurrentTeam(), currWithdrawnMembers, recordDateTime);
    }

    public void putCurrWithdrawnToDataStorage(Date recordDateTime) {
        for (Participant withdrawn : currWithdrawnMembers) {
            RawTeamLevelDismiss rawTeamLevelDismiss =
                    new RawTeamLevelDismiss(getCurrentScanPoint().getScanPointId(), getCurrentTeam().getTeamId(), withdrawn.getUserId(), recordDateTime);
            // init reference fields
            rawTeamLevelDismiss.setScanPoint(getCurrentScanPoint());
            rawTeamLevelDismiss.setTeam(getCurrentTeam());
            rawTeamLevelDismiss.setTeamUser(UsersRegistry.getInstance().getUserById(withdrawn.getUserId()));
            DataStorage.putRawTeamDismiss(rawTeamLevelDismiss);
        }
    }
}
