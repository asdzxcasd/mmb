package ru.mmb.terminal.activity;

public class Constants
{
	public static final boolean UPDATE_FROM_SAVED_BUNDLE = true;
	public static final boolean UPDATE_FOR_FIRST_LAUNCH = false;

	public static final String KEY_CURRENT_SCAN_POINT = "current.scanpoint";
	public static final String KEY_CURRENT_TEAM_NUMBER = "current.team.number";
	public static final String KEY_CURRENT_TEAM = "current.team";
	public static final String KEY_CURRENT_EXPORT_FORMAT = "current.export.format";
	public static final String KEY_CURRENT_INPUT_CHECKPOINTS_STATE =
	    "current.input.checkpoint.state";
	public static final String KEY_CURRENT_INPUT_CHECKED_DATE = "current.input.checked.date";
	public static final String KEY_CURRENT_INPUT_EXISTING_RECORD = "current.input.existing.record";
	public static final String KEY_CURRENT_INPUT_WITHDRAWN_CHECKED =
	    "current.input.withdrawn.checked";
	public static final String KEY_EXPORT_RESULT_MESSAGE = "export.result.message";
	public static final String KEY_LEVEL_SELECT_MODE = "level.select_mode";
	public static final String KEY_REPORT_TEAM_SORT_COLUMN = "report.team.sort.column";
	public static final String KEY_REPORT_TEAM_SORT_ORDER = "report.team.sort.order";
	public static final String KEY_REPORT_TEAM_FILTER_STATE = "report.team.filter.state";
	public static final String KEY_REPORT_TEAM_FILTER_NUMBER_EXACT =
	    "report.team.filter.number.exact";
	public static final String KEY_REPORT_TEAM_FILTER_NUMBER = "report.team.filter.number";
	public static final String KEY_REPORT_TEAM_FILTER_TEAM = "report.team.filter.team";
	public static final String KEY_REPORT_TEAM_FILTER_MEMBER = "report.team.filter.member";
	public static final String KEY_REPORT_TEAM_RESULT_MESSAGE = "report.team.result.message";
	public static final String KEY_REPORT_GLOBAL_REPORT_MODE = "report.global.report.mode";
	public static final String KEY_REPORT_GLOBAL_SELECTED_TEAMS = "report.global.selected.teams";
	public static final String KEY_REPORT_GLOBAL_RESULT_MESSAGE = "report.global.result.message";

	public static final int REQUEST_CODE_DEFAULT_ACTIVITY = -1;
	public static final int REQUEST_CODE_MAIN_ACTIVITY = 1;
	public static final int REQUEST_CODE_SETTINGS_ACTIVITY = 2;
	public static final int REQUEST_CODE_INPUT_HISTORY_ACTIVITY = 4;
	public static final int REQUEST_CODE_INPUT_DATA_ACTIVITY = 5;
	public static final int REQUEST_CODE_WITHDRAW_MEMBER_ACTIVITY = 6;
	public static final int REQUEST_CODE_FILE_DIALOG = 7;
	public static final int REQUEST_CODE_SETTINGS_DB_FILE_DIALOG = 9;
	public static final int REQUEST_CODE_SETTINGS_DEVICE_JSON_DIALOG = 10;
	public static final int REQUEST_CODE_SCAN_POINT_ACTIVITY = 11;
}
