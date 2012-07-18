// this agent search on DBLP

+search_term(ST)[source(S)] : true
   <- 	dblp.scraping_search_term(ST, R);
   		.send(S, tell, send_information(ST, R, _)).
   
+search_citation(CI)[source(S)] : true
   <- 	dblp.scraping_search_citation(CI, R);
   		.send(S, tell, send_filtered_citations(R)).