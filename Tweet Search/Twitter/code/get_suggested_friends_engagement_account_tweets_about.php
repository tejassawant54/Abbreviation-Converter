<?php
// Copyright (c) 2013 Adam Green. All rights reserved.
// See http://140dev.com/download/license.txt for licensing of this code.

function get_suggested_friends_engagement_account_tweets_about($start,$results_per_page) {
	global $db, $engagement_user_id;

	$query = "SELECT users.* 
	FROM users
	WHERE user_id NOT IN 
		(SELECT user_id
		FROM friends)
	AND (user_id IN
		(SELECT target_user_id
		FROM tweet_mentions
		WHERE source_user_id = $engagement_user_id)
		OR user_id IN 
		(SELECT target_user_id
		FROM tweet_retweets
		WHERE source_user_id =$engagement_user_id))
	ORDER BY users.followers_count DESC  
	LIMIT $start, $results_per_page";

	return $db->select_array($query);
}
?>