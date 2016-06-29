package algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.ReviewResponseItem;

public class AverageBasedRanking implements RankingAlgorithm {

	@Override
	
	public List<ReviewResponseItem> rank(
			List<ReviewResponseItem> reviewResponseItems) {
		
	try{
		//reviewResponseItems.sort(new Comparator<ReviewResponseItem>() {   /* for java 8 */

	     Collections.sort(reviewResponseItems, new Comparator<ReviewResponseItem>() {   /* for java 7 */
			@Override
			public int compare(ReviewResponseItem arg0, ReviewResponseItem arg1) {
				// TODO Auto-generated method stub
				String rating1 = arg0.getReviewStatistics()
						.getAverageOverallRating();
				String rating2 = arg1.getReviewStatistics()
						.getAverageOverallRating();
				double rat1 = 0;
				double rat2 = 0;
				try {
					rat1 = Double.parseDouble(rating1);
					rat2 = Double.parseDouble(rating2);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (rat1 == rat2) {
					return 0;
				}
				if (rat1 < rat2)
					return 1;
				else
					return -1;
			}
		});
		
		}
		catch(Exception e){
			
			System.out.println("No proper data");
			System.exit(1);
		}
		return reviewResponseItems;
	}
	}

