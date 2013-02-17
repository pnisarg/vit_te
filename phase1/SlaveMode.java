import java.io.IOException;

public class SlaveMode {
	public void EXECUTEUSERPROGRAM(MasterMode master) throws IOException {
		boolean exit=false;
		int mlocation;
		// TODO: loop terminate condition
		while (!exit) {
			MasterMode.IR = MasterMode.MEMORY[MasterMode.IC];
			MasterMode.IC++;
			
			switch (MasterMode.IR[0]) {
			case 'G': {
				if (MasterMode.IR[1] == 'D') {
					master.MOS(1,getLocation()); //call master mode with SI 1
				}
				break;
			}
			case 'P': {
				if (MasterMode.IR[1] == 'D') {
					master.MOS(2,getLocation()); //call master mode with SI 2
				}
				break;
			}
			case 'S': {
				if (MasterMode.IR[1] == 'R') {
					mlocation=getLocation();
					MasterMode.MEMORY[mlocation]=MasterMode.R;
				}
				break;
			}
			case 'L': {
				if (MasterMode.IR[1] == 'R') {
					mlocation=getLocation();
					MasterMode.R=MasterMode.MEMORY[mlocation];
				}
				break;
			}
			case 'C': {
				if (MasterMode.IR[1] == 'R') {
					mlocation=getLocation();
					if(MasterMode.R[0]==MasterMode.MEMORY[mlocation][0] &&
							MasterMode.R[1]==MasterMode.MEMORY[mlocation][1] &&
							MasterMode.R[2]==MasterMode.MEMORY[mlocation][2] &&
							MasterMode.R[3]==MasterMode.MEMORY[mlocation][3]){
						MasterMode.C=true;
					}else
						MasterMode.C=false;
				}
				break;
			}
			case 'B': {
				if (MasterMode.IR[1] == 'T') {
					if(MasterMode.C){
						MasterMode.IC=(short) getLocation();
					}
				}
				break;
			}
			case 'H': {
				master.MOS(3,0); //call master mode with SI 3
				exit=true;
				break;
			}
			}

		}
		return;
	}

	private int getLocation() {
		int mlocation=0,tempM=0,tempL=0;
		//Extract the memory location from IR
		tempM=(int)MasterMode.IR[2]-48;
		tempL=(int)MasterMode.IR[3]-48;
		mlocation=((tempM*10)+tempL);
		return mlocation;
	}
}
