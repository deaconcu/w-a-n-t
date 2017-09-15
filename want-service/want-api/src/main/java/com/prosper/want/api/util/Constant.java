package com.prosper.want.api.util;

/**
 * 系统配置，一经定义只能添加，不能修改！
 */
public class Constant {

	public static final int ATTENDANCE_MAX = 999;
    
    public static class CacheName {
        public static final String session = "c_session_";
    }
	
	public static class RecommendState {
    	public static final short created = 1;
    	public static final short accepted = 2;
	}

	public static class WantState {
		public static final short created = 1;
		public static final short paused = 2;
		public static final short deleted = 3;
	}
	
}























