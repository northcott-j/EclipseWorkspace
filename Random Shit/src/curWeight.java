// IMobile
// Returns the width  in a given direction starting at this IMobile
int direcMax(String direction);

// Returns the max width in a given direction of this IMobile
// Displacement accounts for the shift caused by struts as
//   the method moves deeper in the IMobile
int traveler(int displace, String direction);

// Returns the entire width of this IMobile
int curWidth();

// Simple

// Returns the protruding portion of this Simple
//   only half goes farther than the strut
//   weight / 10 <-- width / 2 <-- width showing
public int direcMax(String direction) {
	return this.weight / 20;
}

// Returns the protruding portion of this Simple
//   only half goes farther than the strut
//   weight / 10 <-- width / 2 <-- width showing
public int traveler(int displace, String direction) {
	return this.weight / 20;
}

// Returns the entire width of this Simple
public int curWidth() {
	this.width / 10;
}

// Complex

// Returns the width  in a given direction starting at this IMobile
public int direcMax(String direction) {
	if (direction.equals("left")) {
		this.leftside + this.left.direcMax("left");
	}
	else {
		this.rightside + this.right.direcMax("right");
	}
}

// Returns the max width in a given direction of this IMobile
//   Displacement accounts for the shift caused by struts as
//   the method moves deeper in the IMobile
public int traveler(int displace, String direction) {
	if (direction.equals("left")) {
		return Math.max(this.direcMax("left") - displace, 
				        this.right.traveler(displace + this.rightside, "left"));
	}
	else {
		return Math.max(this.direcMax("right") - displace,
						this.left.traveler(displace + this.leftside, "right"));
	}
}

// Returns the entire width of this IMobile
public int curWidth() {
	return this.traveler(0, "left") + this.traveler(0, "right");
}