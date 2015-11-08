The data design I chose was the use an ArrayList of ArrayList of Note. A 
Note contains information such as the pitch (which is an enum of the standard
12), octave, startBeat, endBeat and volume. The ArrayList of Note is
all of the notes played at a single beat. Then the ArrayList of ArrayList 
of Note are the Arrays of each beat and their corresponding Notes.
The Music Editor has a Musical board which is the nested arrays, 
the current beat, the tempo, the lowest octave, the lowest note of the 
lowest octave, the highest octave and the highest note of the highest octave. 
Notes that take up more than one beat are represented by an identical 
reference in the corresponding position in the ArrayList. This way, one 
mutation changes all of them and the Note itself can be picked at from any 
point in its range. 
The Note class handles invariants such as negative values or changing a 
start time to after the end time. The upper level Music Editor handles 
things such as removing references that are no longer needed and delegating 
checking whether or not a change will cause an overlap. 

P.S. My roomates and I bounced ideas off of each other and talked through 
problems, so our representations may be similar, but our implementations 
were done separately. 
P.P.S. If you see node anywhere, I meant to say note. The two words began 
to blur together after awhile and I may have missed changing a couple.
