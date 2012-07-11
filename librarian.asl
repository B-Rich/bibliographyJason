// this agent starts the scraping!

/* plans */

+!start_search(N) : true      // this goal is created by the GUI of the agent 
    <- -+search(N);
       -+count(0, 2);
       .broadcast(tell, search(N)).
       
@pb1[atomic]
+send_information(N, V)[source(S)] : count(C, T) & C < T-1
   <- 	.print("send information from ", S);
   		-count(C, T);
   		+count(C+1, T);
   		get_index(S, N, V).

@pb2[atomic]
+send_information(N, V)[source(S)]: count(C, T) & C = T-1 
   <- 	.print("send information from ", S);
   		get_index(S, N, V);
		.print("Finish");
		show_index(S, N, V);
      	.abolish(send_information(N,_)).