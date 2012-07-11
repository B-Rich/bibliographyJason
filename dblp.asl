// this agent search on DBLP

+search(N)[source(S)] : true
   <- 	scraping.get_information("DBLP", N);
   		.send(S, tell, send_information(N, 7)).
   
