// this agent starts the scraping!
// ST <- Search Term
// R <- Result
// S <- Source
// C <- Count
// T <- Total
// CI <- Citations

// 		get_citations_from_scholar(CI_SCHOLAR)
// 		search_citation(CI_SCHOLAR)
// +send_filtered_citations(R)[source(S)]: true
//    <-	.print("send filtered information from DBLP");
// 		show_filtered_citations(R).
//-+search_term(ST);

//!start_search(ST). //Initial Goal

/* plans */

+!start_search(ST) : true
    <- .print("start_search");
       -+count(0, 2);
       .broadcast(tell, search_term(ST)).
       
+send_information_dblp(ST, R)[source(S)] : true
   <- 	.print("send information from DBLP");
   		get_index(S, ST, R, _);
   		+finish_first_search(S).

+send_information_scholar(ST, R, CI)[source(S)]: true
   <- 	.print("send information from SCHOLAR");
   		get_index(S, ST, R, CI);
   		+finish_first_search(S);
		.print("search_citation in DBLP ", CI);
		.send(dblp, tell, search_citation(CI));
		.abolish(send_information(_,_,_)).
		
+finish_first_search(S): count(C, T) & C < T-1
   <- 	.print("finish_first_search ", S);
   		-count(C, T);
		+count(C+1, T).

+finish_first_search(S): count(C, T) & C = T-1 
   <- 	.print("finish_first_search ", S);
		show_index(S);
		.abolish(send_information(_,_,_)).
		
+send_filtered_citations(R)[source(S)]: true
   <-	.print("send filtered information from DBLP: ", R).