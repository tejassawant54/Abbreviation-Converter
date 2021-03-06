<?php
// Copyright (c) 2013 Adam Green. All rights reserved.
// See http://140dev.com/download/license.txt for licensing of this code.

require('../config.php');
require('../db_lib.php');
$db = new db();

// If the user clicked one of the buttons
if ((isset($_GET['submit'])) || (isset($_GET['prev'])) || (isset($_GET['next']))) {
	$where = '';
	$start_date = htmlspecialchars($_GET['start_date'], ENT_QUOTES);
	if ($start_date != '0000-00-00') {
		$where .= ' AND tweets.created_at >= "' . $db->date($start_date) . '"';
	}
	$end_date = htmlspecialchars($_GET['end_date'], ENT_QUOTES);
	if ($end_date != '0000-00-00') {
		$where .= ' AND tweets.created_at <= "' . $db->date($end_date) . '"';
	}
	if(isset($_GET['prev'])) {
		$page = intval($_GET['page']) - 1;
		if($page<0) {
			$page=0;
		}
	} elseif (isset($_GET['next'])) {
		$page = intval($_GET['page']) + 1;
	} else {
		$page = 0;
	}
	
	// Save the screen name of the user being reported on
	$partner = $db->escape(htmlspecialchars($_GET['partner'], ENT_QUOTES));
	
} else {
	$start_date = '0000-00-00';
	$end_date = '0000-00-00';
	$page = 0;
	$partner = '';
}
	
require('page_top.html');
print '<h2>Engagement Conversation</h2>';

print "<form action='tweet_conversation.php' method='get'>";
print "Start Date: <input type='text' name='start_date' value='$start_date'>";
print "End Date: <input type='text' name='end_date' value='$end_date'><br/>";
print "Conversation Partner: <input type='text' name='partner' value='$partner' size='20'>";
print "<input type='hidden' name='page' value=$page>";
print '<button type="submit" name="submit" value=1>Search</button>';
print '<button type="submit" name="prev" value=1>< Prev</button>';
print '<button type="submit" name="next" value=1>Next ></button>';
print '</form>';

if (!empty($partner)) {
	require('../get_tweet_conversation.php');
	$tweets = get_tweet_conversation($partner,$where,$page*$results_per_page, $results_per_page);	
	require('display_tweets.php');
} else {
	print "No tweets found for this user.";
}

require('page_bottom.html');
?>