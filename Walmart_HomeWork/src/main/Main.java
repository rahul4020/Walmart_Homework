package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Item;
import model.Products;
import model.ReviewResponseItem;
import algorithm.AverageBasedRanking;
import algorithm.RankingAlgorithm;

public class Main {

	public static void main(String[] args) {

		List<ReviewResponseItem> reviewResponseItems = new ArrayList<>();
		try {
			//Step1: get products
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter product to search");
			String queryString = sc.next();
			System.out.println("searching for query string " + queryString + " ...");
			Item item = getProduct(queryString);
			
			
			//Step 2: for first item, get all recomendations for this product id.
			
			List<Item> itemList = null;
			if(item != null) {
				System.out.println("product found");
				System.out.println("item id: " + item.getItemId());
				System.out.println("getting recommendation ...");
				itemList = getReComendatation(item.getItemId());
			}
			
			//itemList contains 10 items. We need to get reviews for each item and rank them.
			//Step 3: for each recommended product, get all reviews.
			if(itemList != null && !itemList.isEmpty()) {
				for(Item tempItem : itemList) {
					ReviewResponseItem responseItem = getResponseItem(tempItem.getItemId());
					if(responseItem != null) {
						reviewResponseItems.add(responseItem);
					}
				}
			} else {
				System.out.println("itemList null or empty");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("No itemList found");
		}
		
		//Step 4: Rank response Items
		//Define Ranking algorithm.
		try{
		RankingAlgorithm rankAlgorithm = new AverageBasedRanking();
	    reviewResponseItems = getRanking(reviewResponseItems, rankAlgorithm);
	    //printing items rankwise
	    System.out.println("++++++++++++++++++++++++++++++");
	    System.out.println("sorting recommendation");
	    System.out.println("+++++++++++++++++++++++++++++++");
	    for(ReviewResponseItem tempItems : reviewResponseItems) {
	    	System.out.println("item id: " + tempItems.getItemId() + " rating : " + tempItems.getReviewStatistics().getAverageOverallRating());
	    }
		}
		catch(Exception e){
				e.getMessage();
				e.printStackTrace();
		}
	}
	
	/**
	 * This method returns first product item.
	 */
	public static Item getProduct(String queryString) {
		try {
			String urlString = "http://api.walmartlabs.com/v1/search?apiKey=7mh698xwjjeewttendcajn9x&query=" + queryString;
			URLConnection  con = new URL(urlString).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
			String inputLine;
			Products products = null;
	        while ((inputLine = in.readLine()) != null) {
	        	//System.out.println(inputLine);
	        	products = Products.newProduct(inputLine);
		        
	        }
	        
	        System.out.println("length :: " + products.getItems().length);
	        in.close();
	        if(products.getItems().length > 0 ) {
	        	Item firstItem = products.getItems()[0];
	        	return firstItem;
	        }
	        
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		} finally {
			
		}
		return null;
		
		}
	
	public static List<Item> getReComendatation(Integer itemId) {
		Item[] items = null;
		List<Item> itemList = new ArrayList<Item>();
		try {
			URLConnection  con = new URL("http://api.walmartlabs.com/v1/nbp?apiKey=7mh698xwjjeewttendcajn9x&itemId=" + itemId).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
			String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	//System.out.println(inputLine);
	        	items = Item.getItems(inputLine);
		        
	        }
	        
	        System.out.println(items.length + " recommendatations found");
	        //get first 10 items
	        for(int i = 0; i < 10 && i < items.length; i++) {
	        	itemList.add(items[i]);
	        }
	        in.close();
	        
	        
		} catch (IOException e) {
//			e.getMessage();
//			e.printStackTrace();
			System.out.println("No recommendation found");
		} finally {
			
		}
		return itemList;
	}
	
	public static ReviewResponseItem getResponseItem(Integer itemId) {
		ReviewResponseItem reviewResponseItem = null;
		try {
			URLConnection  con = new URL("http://api.walmartlabs.com/v1/reviews/" + itemId + "?apiKey=7mh698xwjjeewttendcajn9x&format=json").openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
			String inputLine;
			
	        while ((inputLine = in.readLine()) != null) {
	        	//System.out.println(inputLine);
	        	reviewResponseItem = ReviewResponseItem.getItems(inputLine);
		        
	        }
	  
	        in.close();   
		} catch (IOException e) {
			//e.getMessage();
			//e.printStackTrace();
			System.out.println("Can't get reviews for all the item");
			System.exit(1);  
		} finally {
			
		}
		return reviewResponseItem;
		
	}
	
	public static List<ReviewResponseItem> getRanking(List<ReviewResponseItem> reviewResponseItems, RankingAlgorithm rankAlgorithm) {
		return rankAlgorithm.rank(reviewResponseItems);
	}
}
