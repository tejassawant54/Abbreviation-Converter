<?php 

// The search terms are passed in the q parameter
// search_server.php?q=[search terms]
//added by aish
$con = mysql_connect("localhost","root","","test");
		if (!$con)
		{
			die('Could not connect: ' . mysql_error());
		}
// 		$file = fopen("/Applications/XAMPP/xamppfiles/htdocs/xampp/ProjectGIS/files/template/Twitter/javascript_ebook_source_code/tweets.json","w");
		//end added code
if (!empty($_GET['q'])) {
	
	// Remove any hack attempts from input data
	$search_terms = htmlspecialchars($_GET['q']);
// 	echo $search_terms;
// 	echo "-----";
	// Get the application OAuth tokens
	require 'app_tokens.php';
	
	// Create an OAuth connection
	require 'tmhOAuth.php';
	$connection = new tmhOAuth(array(
	  'consumer_key'    => $consumer_key,
	  'consumer_secret' => $consumer_secret,
	  'user_token'      => $user_token,
	  'user_secret'     => $user_secret
	));
	
	// Request the most recent 100 matching tweets
	$http_code = $connection->request('GET',$connection->url('1.1/search/tweets'), 
		    	array('q' => $search_terms,
		    	'count' => 1000,
		    	'lang' => 'en',
				'type' => 'recent'));

	// Search was successful
	if ($http_code == 200) {
			
		// Extract the tweets from the API response
		$response = json_decode($connection->response['response'],true);
		//echo $response;
		
		echo json_encode($response);
// 		file_put_contents("/Applications/XAMPP/xamppfiles/htdocs/xampp/ProjectGIS/files/template/Twitter/javascript_ebook_source_code/tweets.json", json_encode($response),FILE_APPEND | LOCK_EX) ;

		
		//echo $connection->response['response'] ;
		$tweet_data = $response['statuses']; 
		
		// Load the template for tweet display
		$tweet_template= file_get_contents('tweet_template.html');
		
		// Load the library of tweet display functions
		require 'display_lib.php';	
		
		// Create a stream of formatted tweets as HTML
		$tweet_stream = '';
		foreach($tweet_data as $tweet) {
				
			// Ignore any retweets
			if (isset($tweet['retweeted_status'])) {
				continue;
			}
			
			// Get a fresh copy of the tweet template
			$tweet_html = $tweet_template;
			
			if ($tweet['user']['geo_enabled'] = 1)
			{
			// 	echo $tweet['geo']['coordinates']['0'];
// 				echo $tweet['geo']['coordinates']['1'];
				if (!is_null($tweet['geo']['coordinates']['0']))
				{
					// Insert this tweet into the html
					$tweet_html = str_replace('[screen_name]',
					$tweet['user']['screen_name'],$tweet_html);
					$tweet_html = str_replace('[name]',
					$tweet['user']['name'],$tweet_html);		
					$tweet_html = str_replace('[profile_image_url]',
					$tweet['user']['profile_image_url'],$tweet_html);
					$tweet_html = str_replace('[tweet_id]',
					$tweet['id'],$tweet_html);
					$tweet_html = str_replace('[tweet_text]',
					linkify($tweet['text']),$tweet_html);
					$tweet_html = str_replace('[created_at]',
					twitter_time($tweet['created_at']),$tweet_html);
					$tweet_html = str_replace('[retweet_count]',
					$tweet['retweet_count'],$tweet_html);
					$tweet_html = str_replace('[coordinate0]',
					$tweet['geo']['coordinates']['0'],$tweet_html);			
				    $tweet_html = str_replace('[coordinate1]',
					$tweet['geo']['coordinates']['1'],$tweet_html);			

			
					mysql_select_db('test');
// 					echo ($tweet['created_at']);
					echo "<br>";
					$datecreate = date_create($tweet['created_at']);
					
					$date_format = date_format($datecreate, 'Y-m-d H:i:s');
					$eqdatetime = gmdate($date_format);
					echo $eqdatetime;


// 					$sqlclause = "'".$tweet['user']['location'] ."','".$tweet['user']['screen_name']."','".$tweet['user']['name']."','".$tweet['user']['profile_image_url']."','".$tweet['text']."','". $eqdatetime."'";
// 					$sqlclause = $sqlclause.",'".$tweet['geo']['coordinates']['0']."','".$tweet['geo']['coordinates']['1']."'";
					$sqlclause = "'".$tweet['user']['name']."','".$tweet['text']."'";
// 					. $eqdatetime."'";
// 					$sqlclause = $sqlclause.",'".$tweet['geo']['coordinates']['0']."','".$tweet['geo']['coordinates']['1']."'";

file_put_contents("C:/xampp/htdocs/xampp/Twitter/javascript_ebook_source_code/tweets.json", $sqlclause ,FILE_APPEND | LOCK_EX) ;

					mysql_select_db('test');
					$rs = mysql_query("INSERT INTO earthquake_peopledata values ($sqlclause);");
					
				// 	$res = mysql_query("DELETE FROM earthquake_peopledata USING earthquake_peopledata JOIN (
// 					SELECT @rowid := IF( @f1 = TEXT
// 					AND @f2 = screen_name, @rowid +1, 0 ) AS rowid, @f1 := TEXT AS field1, @f2 := screen_name AS field2, TEXT, screen_name
// 					FROM earthquake_peopledata t, (
// 					SELECT @rowid := NULL , @f1 := NULL , @f2 := NULL
// 					) AS init
// 					ORDER BY field1, field2 DESC
// 					) AS duplicates ON earthquake_peopledata.text = duplicates.text
// 					AND earthquake_peopledata.screen_name = duplicates.screen_name
// 					AND duplicates.rowid >0");
// 					
// 					DELETE FROM earthquake_peopledata USING earthquake_peopledata JOIN (
// SELECT @rowid := IF( @f1 = TEXT
// AND @f2 = screen_name, @rowid +1, 0 ) AS rowid, @f1 := TEXT AS field1, @f2 := screen_name AS field2, TEXT, screen_name
// FROM earthquake_peopledata t, (
// SELECT @rowid := NULL , @f1 := NULL , @f2 := NULL
// ) AS init
// ORDER BY field1, field2 DESC
// ) AS duplicates ON earthquake_peopledata.text = duplicates.text
// AND earthquake_peopledata.screen_name = duplicates.screen_name
// AND duplicates.rowid >0

					echo "<br>";
// 					echo "-------res-----------------";
// 					echo $res; 
			// Add the HTML for this tweet to the stream
					$tweet_stream .= $tweet_html;
		}}
		}
		// Pass the tweets HTML back to the Ajax request
		print $tweet_stream;
		
	// Handle errors from API request
	} else {
		if ($http_code == 429) {
			print 'Error: Twitter API rate limit reached';
		} else {
			print 'Error: Twitter was not able to process that search';
		}
	}

} else {
	print 'No search terms found';
}	

?>