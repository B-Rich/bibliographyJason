// this agent search on Google Scholar

+search(N)[source(S)] : true
   <-	scraping.get_information("SCHOLAR", N);
		.send(S, tell, send_information(N, 6)).
   
