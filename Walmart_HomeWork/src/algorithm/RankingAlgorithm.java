package algorithm;

import java.util.List;

import model.ReviewResponseItem;

public interface RankingAlgorithm {
	List<ReviewResponseItem> rank(List<ReviewResponseItem> reviewResponseItems);
} 
