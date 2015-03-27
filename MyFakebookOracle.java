//akeimach, maxkiss

package project2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;
import java.sql.PreparedStatement;

public class MyFakebookOracle extends FakebookOracle {
	
	static String prefix = "ethanjyx.";
	
	// You must use the following variable as the JDBC connection
	Connection oracleConnection = null;
	
	// You must refer to the following variables for the corresponding tables in your database
	String cityTableName = null;
	String userTableName = null;
	String friendsTableName = null;
	String currentCityTableName = null;
	String hometownCityTableName = null;
	String programTableName = null;
	String educationTableName = null;
	String eventTableName = null;
	String participantTableName = null;
	String albumTableName = null;
	String photoTableName = null;
	String coverPhotoTableName = null;
	String tagTableName = null;
	
	// DO NOT modify this constructor
	public MyFakebookOracle(String u, Connection c) {
		super();
		String dataType = u;
		oracleConnection = c;
		// You will use the following tables in your Java code
		cityTableName = prefix+dataType+"_CITIES";
		userTableName = prefix+dataType+"_USERS";
		friendsTableName = prefix+dataType+"_FRIENDS";
		currentCityTableName = prefix+dataType+"_USER_CURRENT_CITY";
		hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITY";
		programTableName = prefix+dataType+"_PROGRAMS";
		educationTableName = prefix+dataType+"_EDUCATION";
		eventTableName = prefix+dataType+"_USER_EVENTS";
		albumTableName = prefix+dataType+"_ALBUMS";
		photoTableName = prefix+dataType+"_PHOTOS";
		tagTableName = prefix+dataType+"_TAGS";
	}
	
	
	@Override
	// ***** Query 0 *****
	// This query is given to your for free;
	// You can use it as an example to help you write your own code
	public void findMonthOfBirthInfo() throws SQLException{ 
		ResultSet rst = null; 
		PreparedStatement getMonthCountStmt = null;
		PreparedStatement getNamesMostMonthStmt = null;
		PreparedStatement getNamesLeastMonthStmt = null;
		
		try {
			// For each month, find the number of friends born that month
			// Sort them in descending order of count
			String getMonthCountSql = "SELECT COUNT(*), month_of_birth FROM "
					+ userTableName + " WHERE month_of_birth IS NOT NULL GROUP BY month_of_birth ORDER BY 1 DESC";
			getMonthCountStmt = oracleConnection.prepareStatement(getMonthCountSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getMonthCountStmt.executeQuery();
			
			// Get the month with most friends, the month with least friends, total friends listed month_of_birth
			this.monthOfMostFriend = 0;
			this.monthOfLeastFriend = 0;
			this.totalFriendsWithMonthOfBirth = 0;
			while(rst.next()) {
				int count = rst.getInt(1);
				int month = rst.getInt(2);
				if (rst.isFirst())
					this.monthOfMostFriend = month;
				if (rst.isLast())
					this.monthOfLeastFriend = month;
				this.totalFriendsWithMonthOfBirth += count;
			}
			
			// Get the names of friends born in the "most" month
			String getNamesMostMonthSql = "SELECT user_id, first_name, last_name FROM "
					+ userTableName + " WHERE month_of_birth = ?";
			getNamesMostMonthStmt = oracleConnection.prepareStatement(getNamesMostMonthSql);
			// set the first ? in the sql above to value this.monthOfMostFriend, with Integer type
			getNamesMostMonthStmt.setInt(1, this.monthOfMostFriend);
			rst = getNamesMostMonthStmt.executeQuery();
			
			while(rst.next()) {
				Long uid = rst.getLong(1);
				String firstName = rst.getString(2);
				String lastName = rst.getString(3);
				this.friendsInMonthOfMost.add(new UserInfo(uid, firstName, lastName));
			}
			
			// Get the names of friends born in the "least" month
			String getNamesLeastMonthSql = "SELECT first_name, last_name, user_id FROM " 
					+ userTableName + " WHERE month_of_birth = ?";
			getNamesLeastMonthStmt = oracleConnection.prepareStatement(getNamesLeastMonthSql);
			getNamesLeastMonthStmt.setInt(1, this.monthOfLeastFriend);
			rst = getNamesLeastMonthStmt.executeQuery();
			
			while(rst.next()){
				String firstName = rst.getString(1);
				String lastName = rst.getString(2);
				Long uid = rst.getLong(3);
				this.friendsInMonthOfLeast.add(new UserInfo(uid, firstName, lastName));
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			// can do more things here
			throw e;	
		}
		
		finally {
			// Close statement and result set
			if(rst != null) 
				rst.close();
			if(getMonthCountStmt != null)
				getMonthCountStmt.close();
			if(getNamesMostMonthStmt != null)
				getNamesMostMonthStmt.close();
			if(getNamesLeastMonthStmt != null)
				getNamesLeastMonthStmt.close();
		}
	}

	
	
	@Override
	// ***** Query 1 *****
	// Find information about friend names:
	// (1) The longest last name (if there is a tie, include all in result)
	// (2) The shortest last name (if there is a tie, include all in result)
	// (3) The most common last name, and the number of times it appears (if there is a tie, include all in result)
	public void findNameInfo() throws SQLException { // Query1
		ResultSet rst = null; 
		PreparedStatement getLastNamesStmt = null;
		
		try {
			String getNamesLengthSql = "SELECT last_name, LENGTH(last_name), COUNT(last_name) FROM "
					+ userTableName + " WHERE last_name IS NOT NULL GROUP BY last_name ORDER BY 2 DESC";
			getLastNamesStmt = oracleConnection.prepareStatement(getNamesLengthSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getLastNamesStmt.executeQuery();

			//get longest names first, check popular names
			int maxLength = 0;
			int minLength = 0;
			int maxPopularity = 0;
			int currLength = 0;
			int currPopularity = 0;
			String lastName = null;
			while (rst.next()) {
				lastName = rst.getString(1);
				currLength = rst.getInt(2);
				currPopularity = rst.getInt(3);
				if (rst.isFirst()) {
					this.longestLastNames.add(lastName);
					maxLength = currLength;
				}
				if (maxLength == currLength) {
					this.longestLastNames.add(lastName);
				}
				if (maxPopularity < currPopularity) {
					maxPopularity = currPopularity;
				}
			}
			
			//work backwards to get shortest, record popular
			while (rst.previous()) {
				lastName = rst.getString(1);
				currLength = rst.getInt(2);
				currPopularity = rst.getInt(3);
				if (rst.isLast()) {
					this.shortestLastNames.add(lastName);
					minLength = currLength;
				}
				if (minLength == currLength) {
					this.shortestLastNames.add(lastName);
				}
				if (maxPopularity == currPopularity) {
					this.mostCommonLastNames.add(lastName);
					this.mostCommonLastNamesCount = maxPopularity;
				}
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getLastNamesStmt != null) 
				getLastNamesStmt.close();
		}
	}
	
	@Override
	// ***** Query 2 *****
	// Find the user(s) who have strictly more than 80 friends in the network
	// Be careful on this query!
	// Remember that if two users are friends, the friends table
	// only contains the pair of user ids once, subject to 
	// the constraint that user1_id < user2_id
	public void popularFriends() throws SQLException {
		ResultSet rst = null;
		PreparedStatement getPopularFriendsStmt = null;
		
		try {
			String getPopularFriendsSql = "SELECT U.user_id, U.first_name, U.last_name FROM " 
					+ userTableName + " U WHERE U.user_id IN (SELECT A.user_id FROM " 
					+ userTableName + " A, " 
					+ friendsTableName + " B WHERE A.user_id = B.user1_id OR A.user_id = B.user2_id GROUP BY A.user_id HAVING COUNT(*) > 80)";
			getPopularFriendsStmt = oracleConnection.prepareStatement(getPopularFriendsSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getPopularFriendsStmt.executeQuery();
			
			this.countPopularFriends = 0;
			while (rst.next()) {
				Long uid = rst.getLong(1);
				String firstName = rst.getString(2);
				String lastName = rst.getString(3);
				this.popularFriends.add(new UserInfo(uid, firstName, lastName));
				this.countPopularFriends += 1;
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getPopularFriendsStmt != null) 
				getPopularFriendsStmt.close();
		}
	}
	 

	@Override
	// ***** Query 3 *****
	// Find the users who still live in their hometowns
	// (I.e., current_city = hometown_city)
	public void liveAtHome() throws SQLException {
		PreparedStatement getHomebodyStmt = null;
		ResultSet rst = null;

		try {
			String getHomebodySql = "SELECT U.user_id, U.first_name, U.last_name FROM " 
					+ userTableName + " U, " 
					+ currentCityTableName + " C, " 
					+ hometownCityTableName + " H WHERE U.user_id = C.user_id AND U.user_id = H.user_id AND C.current_city_id = H.hometown_city_id";
			getHomebodyStmt = oracleConnection.prepareStatement(getHomebodySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getHomebodyStmt.executeQuery();
			
			this.countLiveAtHome = 0;
			while (rst.next()) {
				this.liveAtHome.add(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
				this.countLiveAtHome += 1;
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getHomebodyStmt != null)
				getHomebodyStmt.close();
		}
	}


	@Override
	// **** Query 4 ****
	// Find the top-n photos based on the number of tagged users
	// If there are ties, choose the photo with the smaller numeric PhotoID first
	public void findPhotosWithMostTags(int n) throws SQLException { 
		ResultSet rst = null;
		PreparedStatement getTopPhotosStmt = null;
		
		try {
			String getTopPhotosSql = "SELECT P.photo_id, A.album_id, A.album_name, P.photo_caption, P.photo_link, U.user_id, U.first_name, U.last_name FROM (SELECT T.tag_photo_id, T.tag_subject_id FROM (SELECT tag_photo_id FROM (SELECT tag_photo_id, COUNT(tag_subject_id) FROM "
					+ tagTableName + " WHERE tag_subject_id IS NOT NULL GROUP BY tag_photo_id ORDER BY 2 DESC, 1 ASC) WHERE ROWNUM <= ?) ID INNER JOIN "
					+ tagTableName + " T ON ID.tag_photo_id = T.tag_photo_id) T, "
					+ photoTableName + " P, "
					+ albumTableName + " A, "
					+ userTableName + " U WHERE T.tag_photo_id = P.photo_id AND T.tag_subject_id = U.user_id AND P.album_id = A.album_id ORDER BY 1 ASC";
			getTopPhotosStmt = oracleConnection.prepareStatement(getTopPhotosSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			getTopPhotosStmt.setInt(1, n);
			rst = getTopPhotosStmt.executeQuery();
			
			rst.next(); //start sequence
			for (int i = 0; i < n; i++) {
				String photoId = rst.getString(1);
				String albumId = rst.getString(2);
				String albumName = rst.getString(3);
				String photoCaption = rst.getString(4);
				String photoLink = rst.getString(5);
				PhotoInfo p = new PhotoInfo(photoId, albumId, albumName, photoCaption, photoLink);
				TaggedPhotoInfo tp = new TaggedPhotoInfo(p);
				
				do {
					if (!photoId.equals(rst.getString(1))) break;
						long userId = rst.getLong(6);
						String firstName = rst.getString(7);
						String lastName = rst.getString(8);
						tp.addTaggedUser(new UserInfo(userId, firstName, lastName));
						photoId = rst.getString(1);
				} while (rst.next());
				
				this.photosWithMostTags.add(tp);
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getTopPhotosStmt != null)
				getTopPhotosStmt.close();
		}
	}

	
	
	@Override
	// **** Query 5 ****
	// Find suggested "match pairs" of friends, using the following criteria:
	// (1) One of the friends is female, and the other is male
	// (2) Their age difference is within "yearDiff"
	// (3) They are not friends with one another
	// (4) They should be tagged together in at least one photo
	// You should up to n "match pairs"
	// If there are more than n match pairs, you should break ties as follows:
	// (i) First choose the pairs with the largest number of shared photos
	// (ii) If there are still ties, choose the pair with the smaller user_id for the female
	// (iii) If there are still ties, choose the pair with the smaller user_id for the male
	public void matchMaker(int n, int yearDiff) throws SQLException { 
		ResultSet rst = null;
		PreparedStatement getMatchStmt = null;

		try {
			String matchStrng = "SELECT A.user_id, B.user_id, COUNT(TAG.tag_photo_id), A.first_name, A.last_name, B.first_name, B.last_name, A.year_of_birth, B.year_of_birth, P.photo_id, ALB.album_id, ALB.album_name, P.photo_caption, P.photo_link FROM "
					+ userTableName + " A, " 
					+ userTableName + " B, " 
					+ tagTableName + " TAG, " 
					+ albumTableName + " ALB, " 
					+ photoTableName + " P WHERE TAG.tag_subject_id = A.user_id AND EXISTS (SELECT TAG2.tag_photo_id FROM " 
					+ tagTableName + " TAG2 WHERE TAG.tag_photo_id = TAG2.tag_photo_id AND TAG2.tag_subject_id = B.user_id) AND A.gender = 'male' AND B.gender = 'female' AND NOT EXISTS (SELECT user1_id, user2_id FROM " 
					+ friendsTableName + " WHERE A.user_id = user1_id AND B.user_id = user2_id) AND NOT EXISTS (SELECT user1_id, user2_id FROM " 
					+ friendsTableName + " WHERE B.user_id = user1_id AND A.user_id = user2_id) AND ABS(A.year_of_birth - B.year_of_birth) <= " 
					+ yearDiff + " AND TAG.tag_photo_id = P.photo_id AND P.album_id = ALB.album_id GROUP BY A.user_id, B.user_id, A.first_name, A.last_name, B.first_name, B.last_name, A.year_of_birth, B.year_of_birth, P.photo_id, ALB.album_id, ALB.album_name, P.photo_caption, P.photo_link ORDER BY 3 DESC, 2 ASC, 1 ASC";
			getMatchStmt = oracleConnection.prepareStatement(matchStrng, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getMatchStmt.executeQuery();

			for(int i = 0; i < n; i++) {
				if(!rst.next()) break;
				Long boyUserId = rst.getLong(1);
				Long girlUserId = rst.getLong(2);
				String boyFirstName = rst.getString(4);
				String boyLastName = rst.getString(5);
				String girlFirstName = rst.getString(6);
				String girlLastName = rst.getString(7);
				int boyYear = rst.getInt(8);
				int girlYear = rst.getInt(9);
				MatchPair mp = new MatchPair(girlUserId, girlFirstName, girlLastName, girlYear, boyUserId, boyFirstName, boyLastName, boyYear);
			
				String sharedPhotoId = rst.getString(10);
				String sharedPhotoAlbumId = rst.getString(11);
				String sharedPhotoAlbumName = rst.getString(12);
				String sharedPhotoCaption = rst.getString(13);
				String sharedPhotoLink = rst.getString(14);
				mp.addSharedPhoto(new PhotoInfo(sharedPhotoId, sharedPhotoAlbumId, sharedPhotoAlbumName, sharedPhotoCaption, sharedPhotoLink));
				this.bestMatches.add(mp);
			}
		}

		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}

		finally {
			if(rst != null) 
				rst.close();
			if(getMatchStmt != null)
				getMatchStmt.close();
		}
	}

	
	
	// **** Query 6 ****
	// Suggest friends based on mutual friends
	// Find the top n pairs of users in the database who share the most
	// friends, but such that the two users are not friends themselves.
	// Your output will consist of a set of pairs (user1_id, user2_id)
	// No pair should appear in the result twice; you should always order the pairs so that
	// user1_id < user2_id
	// If there are ties, you should give priority to the pair with the smaller user1_id.
	// If there are still ties, give priority to the pair with the smaller user2_id.
	@Override
	public void suggestFriendsByMutualFriends(int n) throws SQLException {
		ResultSet rst1 = null;
		ResultSet rst2 = null;
		PreparedStatement getNonFriendsStmt = null;
		PreparedStatement getMutualFriendsStmt = null;
	
		try {
			String getNonFriendsSql = "SELECT mutCount, uA, uB, firstA, lastA, firstB, lastB FROM (SELECT COUNT(*) AS mutCount, THIRD.uA, THIRD.uB, THIRD.firstA, THIRD.lastA, THIRD.firstB, THIRD.lastB FROM (SELECT A1.checkID AS uA, A1.first_Name AS firstA, A1.last_Name AS lastA , B1.checkID AS uB, B1.first_Name AS firstB, B1.last_Name AS lastB FROM (SELECT A2.user_id AS checkID, A2.first_Name, A2.last_Name , B2.user2_id AS friend FROM "
					+ userTableName + " A2, "
					+ friendsTableName + " B2 WHERE A2.user_id = B2.user1_id UNION SELECT A2.user_id AS checkID,A2.first_Name, A2.last_Name, B2.user1_id AS friend FROM "
					+ userTableName + " A2, "
					+ friendsTableName + " B2 WHERE A2.user_id=B2.user2_id) A1, (SELECT A2.user_id AS checkID, A2.first_Name, A2.last_Name ,B2.user2_id AS friend FROM ethanjyx.public_users A2, ethanjyx.public_friends B2 WHERE A2.user_id=B2.user1_id UNION SELECT A2.user_id AS checkID, A2.first_Name, A2.last_Name, B2.user1_id AS friend FROM "
			        + userTableName + " A2, "
			        + friendsTableName + " B2 WHERE A2.user_id=B2.user2_id) B1 WHERE A1.checkID <> B1.checkID AND A1.friend = B1.friend) THIRD group by THIRD.uA, THIRD.uB, THIRD.firstA, THIRD.lastA, THIRD.firstB, THIRD.lastB order by 1 DESC)E WHERE uA < uB AND  (uA, uB) not IN (SELECT user1_id, user2_id FROM "
			    	+ friendsTableName + ") AND (uB, uA) not IN (SELECT user1_id, user2_id FROM "
			    	+ friendsTableName + ") ORDER BY mutCount DESC, uA ASC, uB ASC";
			getNonFriendsStmt = oracleConnection.prepareStatement(getNonFriendsSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst1 = getNonFriendsStmt.executeQuery();
			int noNewFriends = 0;
			int mutualFriends = 0;
			
			while(rst1.next() && noNewFriends < n) {

	            mutualFriends  = rst1.getInt(1);
	            Long user1_id = rst1.getLong(2);
	            Long user2_id = rst1.getLong(3);
	            String user1FirstName = rst1.getString(4);
	            String user1LastName = rst1.getString(5);
	            String user2FirstName = rst1.getString(6);
	            String user2LastName = rst1.getString(7);
	        
	            FriendsPair p = new FriendsPair(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
	            
	            String getMutualFriendsSql = "SELECT DISTINCT u2.user_id, u2.first_name, u2.last_name FROM (SELECT U.user_id, F.user2_id FROM "
	            		+ userTableName + " U, "
	            		+ friendsTableName + " F WHERE U.user_id = F.user1_id UNION SELECT U.user_id, F.user1_id FROM "
	            		+ userTableName + " U, "
	            		+ friendsTableName + " F WHERE U.user_id = F.user2_id) A, (SELECT U.user_id, F.user2_id FROM "
	            		+ userTableName + " U, " 
	            		+ friendsTableName + " F WHERE U.user_id = F.user1_id UNION SELECT U.user_id, F.user1_id FROM "
	            		+ userTableName +" U, "
	            		+ friendsTableName + " F WHERE U.user_id = F.user2_id) B, "
	            		+ userTableName + " u2 WHERE A.user_id <> B.user_id AND A.user2_id = B.user2_id AND A.user_id = ?  AND B.user_id = ? AND u2.user_id = A.user2_id";
	            getMutualFriendsStmt = oracleConnection.prepareStatement(getMutualFriendsSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        	getMutualFriendsStmt.setLong(1, user1_id);
	        	getMutualFriendsStmt.setLong(2, user2_id);
	        	rst2 = getMutualFriendsStmt.executeQuery();

	            for (int i = 0; i < mutualFriends; i++) {
	                
	                rst2.next();

	                Long user_id = rst2.getLong(1);
	                String friendFirstName = rst2.getString(2);
	                String friendLastName = rst2.getString(3);
	                p.addSharedFriend(user_id, friendFirstName, friendLastName);    
	            }
	            
	            this.suggestedFriendsPairs.add(p);
	            noNewFriends++;
	            
	        }

		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst1 != null) 
				rst1.close();
			if(getNonFriendsStmt != null)
				getNonFriendsStmt.close();
			if(rst2 != null)
				rst2.close();
			if(getMutualFriendsStmt != null)
				getMutualFriendsStmt.close();

		}
	}
	
	
	
	//@Override
	// ***** Query 7 *****
	// Given the ID of a user, find information about that
	// user's oldest friend and youngest friend
	// If two users have exactly the same age, meaning that they were born
	// on the same day, then assume that the one with the larger user_id is older
	public void findAgeInfo(Long user_id) throws SQLException {
		ResultSet rst = null;
		PreparedStatement getExtremeAgesStmt = null;
		
		try {
			String getExtremeAgesSql = "SELECT U.year_of_birth, U.month_of_birth, U.day_of_birth, U.user_id, U.first_name, U.last_name FROM (SELECT user1_id FROM "
					+ friendsTableName + " WHERE user2_id = ? UNION SELECT user2_id FROM "
					+ friendsTableName + " WHERE user1_id = ?) F INNER JOIN "
					+ userTableName + " U ON F.user1_id = U.user_id ORDER BY 1 ASC, 2 ASC, 3 ASC, 4 DESC";
			getExtremeAgesStmt = oracleConnection.prepareStatement(getExtremeAgesSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			getExtremeAgesStmt.setLong(1, user_id);
			getExtremeAgesStmt.setLong(2, user_id);
			rst = getExtremeAgesStmt.executeQuery();
			
			while(rst.next()) {
				if (rst.isFirst())
					this.oldestFriend = new UserInfo(rst.getLong(4), rst.getString(5), rst.getString(6));
				if (rst.isLast())
					this.youngestFriend = new UserInfo(rst.getLong(4), rst.getString(5), rst.getString(6));
			}	
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getExtremeAgesStmt != null)
				getExtremeAgesStmt.close();
		}
	}
	
	
	@Override
	// ***** Query 8 *****
	// Find the name of the city with the most events, as well as the number of 
	// events in that city.  If there is a tie, return the names of all of the (tied) cities.
	public void findEventCities() throws SQLException {
		ResultSet rst = null;
		PreparedStatement getTopEventsStmt = null;
		
		try {
			String getTopEventsSql = "SELECT C.city_name, COUNT(E.event_city_id) FROM "
					+ eventTableName + " E, " 
					+ cityTableName + " C WHERE C.city_id = E.event_city_id GROUP BY C.city_name HAVING COUNT(E.event_id) = (SELECT MAX(COUNT(*)) FROM "
					+ eventTableName + " GROUP BY event_city_id)";
			getTopEventsStmt = oracleConnection.prepareStatement(getTopEventsSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getTopEventsStmt.executeQuery();
			
			while(rst.next()) {
				int count = rst.getInt(2);
				String cityName = rst.getString(1);
				this.eventCount = count;
				this.popularCityNames.add(cityName);
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		
		finally {
			if(rst != null) 
				rst.close();
			if(getTopEventsStmt != null)
				getTopEventsStmt.close();
		}
	}
	
	
	
	@Override
	//	 ***** Query 9 *****
	// Find pairs of potential siblings and print them out in the following format:
	//   # pairs of siblings
	//   sibling1 lastname(id) and sibling2 lastname(id)
	//   siblingA lastname(id) and siblingB lastname(id)  etc.
	// A pair of users are potential siblings if they have the same last name and hometown, if they are friends, and
	// if they are less than 10 years apart in age.  Pairs of siblings are returned with the lower user_id user first
	// on the line.  They are ordered based on the first user_id and in the event of a tie, the second user_id.
	public void findPotentialSiblings() throws SQLException {
		ResultSet rst = null;
		PreparedStatement getPotSibStmt = null;

		try {
			String sibString = "SELECT A.user_id, B.user_id, A.first_name, B.first_name, A.last_name, B.last_name FROM " 
					+ friendsTableName + " checkFriends, " 
					+ userTableName + " A, " 
					+ userTableName + " B, " 
					+ hometownCityTableName + " cityA, " 
					+ hometownCityTableName + " cityB WHERE A.last_name = B.last_name AND cityA.hometown_city_id = cityB.hometown_city_id AND cityA.user_id = A.user_id AND cityB.user_id = B.user_id AND checkFriends.user1_id = A.user_id AND checkFriends.user2_id = B.user_id AND ABS(A.year_of_birth - B.year_of_birth) < 10 ORDER BY 1 ASC, 2 ASC";
			getPotSibStmt = oracleConnection.prepareStatement(sibString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rst = getPotSibStmt.executeQuery();
	
			while (rst.next()) {
				Long user1_id = rst.getLong(1);
				Long user2_id = rst.getLong(2);
				String user1FirstName = rst.getString(3);
				String user2FirstName = rst.getString(4);
				String user1LastName = rst.getString(5);
				String user2LastName = rst.getString(6);
				SiblingInfo s = new SiblingInfo(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
				this.siblings.add(s);
			}
		}
		
		catch (SQLException e) {
			System.err.println(e.getMessage());
			throw e;
		}

		finally {
			if(rst != null) 
				rst.close();
			if(getPotSibStmt != null)
				getPotSibStmt.close();
		}
	}
}
