// this agent search on Google Scholar

+search_term(ST)[source(S)] : true
   <-	.print("search_term SCHOLAR");
   		scholar.scraping_search_term(ST, R, CI);
		.send(S, tell, send_information_scholar(ST, R, CI)).