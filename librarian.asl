// this agent starts the auction and identify the winner

/* beliefs and rules */ 

all_information_received(N) :- .count(send_information(N, V), 2). 

/* plans */

+!start_search(N) : true      // this goal is created by the GUI of the agent 
    <- -+search(N);
       -+count(0, 2);
       .broadcast(tell, search(N)).
       
@pb1[atomic]
+send_information(N, V)[source(S)] : count(C, T) & C < T-1
   <- 	.print("send information from ", S);
   		-count(C, T);
   		+count(C+1, T).

@pb2[atomic]
+send_information(N, V)[source(S)]: count(C, T) & C = T-1 
   <- 	.print("send information from ", S);
		!check_end(N).

+!check_end(N) : all_information_received(N)
   <- 	.print("Finish");
      	.abolish(send_information(N,_)).