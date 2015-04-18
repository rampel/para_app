package com.luna.web;

public class WebURL {

	public static String CATEGORIES = "http://tinigpinoy.net/?oJSON=89172938729183&type=categories";
	public static String NEWS_BY_CATEGORY = "http://tinigpinoy.net/?oJSON=89172938729183&cat=";
	public static String NEWS_LIST = "http://tinigpinoy.net/?oJSON=89172938729183&page=";
	public static String NEWS_DETAILS = "http://tinigpinoy.net/?oJSON=89172938729183&post_id=%@&type=post";
	public static String DJ_LIST = "http://tinigpinoy.net/?oJSON=89172938729183&author_id=%@";
	public static String DJ_DETAILS = "http://tinigpinoy.net/?oJSON=89172938729183&type=dj&user_id=";
	public static String DJ_SHOW = "http://tinigpinoy.net/?oJSON=89172938729183&type=allshows&author_id=";
	public static String DJ_NEWS = "http://tinigpinoy.net/?oJSON=89172938729183&author_id=";
	public static String SHOW_DETAILS = "http://tinigpinoy.net/?oJSON=89172938729183&type=show&show_id=";
	

	// ALL DJ = http://tinigpinoy.net/?oJSON=89172938729183&type=alldjs
	// DJ DETAILS = [NSString
	// stringWithFormat:@"http://tinigpinoy.net/?oJSON=89172938729183&type=dj&user_id=%@",
	// userID];
 
	// NEWS PER DETAIL = [NSString
	// stringWithFormat:@"http://tinigpinoy.net/?oJSON=89172938729183&author_id=%@",
	// userID]
	// PULL SHOWS PER DETAIL = [NSString
	// stringWithFormat:@"http://tinigpinoy.net/?oJSON=89172938729183&type=allshows&author_id=%@",
	// userID];

	// SHOWS PAGING = [NSString
	// stringWithFormat:@"http://tinigpinoy.net/?oJSON=89172938729183&type=allshows&page=%d",
	// _pageNumber];
	// SHOW DETAIL = [NSString
	// stringWithFormat:@"http://tinigpinoy.net/?oJSON=89172938729183&type=show&show_id=%@",
	// showID];
}
