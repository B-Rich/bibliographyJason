// this agent search on Google Scholar

+search_term(ST)[source(S)] : true
   <-	scholar.scraping_search_term(ST, R, CI);
		.send(S, tell, send_information(ST, R, CI)).
   
