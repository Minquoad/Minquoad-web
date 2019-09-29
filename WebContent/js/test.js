console.log("test3");

class Truc {

	constructor(value) {
		this.value = value;
	}

	log() {
		console.log(this.value);
	}
}

let truc = new Truc(0);

truc.log();
truc.value = 1;
truc.log();
truc.log = () => console.log(2);
truc.log();
truc.value = 3;
truc.log();
