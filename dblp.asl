// this agent search on DBLP

+search_term(ST)[source(S)] : true
   <- 	.print("search_term DBLP");
   		dblp.scraping_search_term(ST, R);
   		.send(S, tell, send_information_dblp(ST, R)).
   		
+search_citation(CI)[source(S)] : true
    <- 	.print("search citation DBLP");
    	dblp.scraping_search_citation(CI, R);
    	.print("result: ", R);
        .send(S, tell, send_filtered_citations(R)).