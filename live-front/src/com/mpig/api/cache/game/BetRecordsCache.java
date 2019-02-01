package com.mpig.api.cache.game;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.mpig.api.db.DataSource;
import com.mpig.api.utils.LocalObject;
import com.mpig.api.utils.VarConfigUtils;

public class BetRecordsCache{
	private static final Logger logger = Logger.getLogger(BetRecordsCache.class);
	public static final LocalObject<BetRecordsCache> instatnce = new LocalObject<BetRecordsCache>(){
		@Override
		protected BetRecordsCache create() {
			return new BetRecordsCache();
		}
	};
	
	private AtomicLong invalidateTs = new AtomicLong();
	private static final int durationPerRound = 30 * 1000;
	private CopyOnWriteArrayList<BetRecord> cache = new CopyOnWriteArrayList<BetRecord>();
	private List<BetRecord> unmodifiedCache = Collections.unmodifiableList(cache);
	
	public List<BetRecord> getLatestRecords() {
		long curTs = System.currentTimeMillis();
		if(invalidateTs.get() <= curTs){
			cache.clear();
			List<BetRecord> latestRecordsFromRDS = getLatestRecordsFromRDS();
			if(!latestRecordsFromRDS.isEmpty()){
				cache.addAll(latestRecordsFromRDS);
			}
			invalidateTs.set(curTs + durationPerRound);
		}
		return unmodifiedCache;
	}

	private List<BetRecord> getLatestRecordsFromRDS() {
		List<BetRecord> records = new ArrayList<BetRecord>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuGame).getConnection();
			String strBet = "select innernid,outernid,add_time from game_bet_result order by add_time desc limit 10;";
			stmt = conn.prepareStatement(strBet);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int innernid = rs.getInt("innernid");
				int outernid = rs.getInt("outernid");
				int add_time = rs.getInt("add_time");
				records.add(new BetRecordsCache.BetRecord(add_time, innernid, outernid));
			}

		} catch (Exception ex) {
			logger.error("BetRecordDao getLatestRecords:", ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}
	
	public class BetRecord implements Serializable {
		private static final long serialVersionUID = 1312981333710896417L;
		private int time;
		private int inner_id;
		private int outer_id;

		public BetRecord(int time, int inner_id, int outer_id) {
			this.time = time;
			this.inner_id = inner_id;
			this.outer_id = outer_id;
		}

		public int getInner_id() {
			return inner_id;
		}

		public void setInner_id(int inner_id) {
			this.inner_id = inner_id;
		}

		public int getOuter_id() {
			return outer_id;
		}

		public void setOuter_id(int outer_id) {
			this.outer_id = outer_id;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}
}

