package model;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ReviewResponseItem {
	private String itemId;
	private ReviewStatistics reviewStatistics;
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public ReviewStatistics getReviewStatistics() {
		return reviewStatistics;
	}

	public void setReviewStatistics(ReviewStatistics reviewStatistics) {
		this.reviewStatistics = reviewStatistics;
	}

	

	public static ReviewResponseItem getItems(String json) throws JsonParseException,
			JsonMappingException, IOException {
		// System.out.println("json" + json);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, ReviewResponseItem.class);
	}
}
