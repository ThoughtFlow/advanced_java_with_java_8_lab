package lock_moviedb;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessRecord {

	private static AtomicInteger nextRecordId = new AtomicInteger(1);
	private final String accessId;
	private final Integer recordId;
	private final String method;
	private final long startTime;
	private long endTime;

	public static AccessRecord recordStart(String accessId, String method) {
		return new AccessRecord(nextRecordId.addAndGet(1), accessId, method, System.nanoTime());
	}
	
	public static AccessRecord recordEnd(AccessRecord startRecord) {
		startRecord.endTime = System.nanoTime();
		
		return startRecord;
	}
	
	private AccessRecord(Integer recordId, String id, String method, long startTime) {
		this.recordId = recordId;
		this.accessId = id;
		this.method = method;
		this.startTime = startTime;
	}
	
	public Integer getRecordId() {
		return recordId;
	}

	public String getId() {
		return accessId;
	}
	
	public boolean isOverlap(AccessRecord compare) {
		return (startTime > compare.startTime && startTime < compare.endTime) ||
			   (endTime > compare.startTime && endTime < compare.endTime);
	}

	@Override
	public String toString() {
		return "AccessRecord [accessId=" + accessId + ", recordId=" + recordId + ", method=" + method + ", startTime="
				+ startTime + ", endTime=" + endTime + "]";
	}
	
	
}
