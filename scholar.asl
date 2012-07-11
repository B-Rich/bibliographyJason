// this agent search on Google Scholar

+search(N)[source(S)] : true
   <-	scraping.get_information("SCHOLAR", N, V);
		.send(S, tell, send_information(N, V)).
   
