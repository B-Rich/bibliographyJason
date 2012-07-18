// this agent starts the scraping!
// ST <- Search Term
// R <- Result
// S <- Source
// C <- Count
// T <- Total
// CI <- Citations

/* plans */

+!start_search(ST) : true      // this goal is created by the GUI of the agent 
    <- -+search(ST);
       -+count(0, 2);
       .broadcast(tell, search(N)).
       
@pb1[atomic]
+send_information(ST, R, CI)[source(S)] : count(C, T) & C < T-1
   <- 	.print("send information from ", S);
   		-count(C, T);
   		+count(C+1, T);
   		get_index(S, ST, R).

@pb2[atomic]
+send_information(ST, R, CI)[source(S)]: count(C, T) & C = T-1 
   <- 	.print("send information from ", S);
   		get_index(S, ST, R);
		.print("Finish");
		show_index(S, ST, R);
		get_citations_from_scholar(CI_SCHOLAR)
		search_citation(CI_SCHOLAR)
      	.abolish(send_information(_,_,_)).
      	
+send_filtered_citations(R)[source(S)]: true
   <-	.print("send filtered information from DBLP");
		show_filtered_citations(R).