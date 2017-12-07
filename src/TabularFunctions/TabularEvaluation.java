package TabularFunctions;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import LinkedList.SLinkedList;
import LinkedList.SNode;
import sun.nio.cs.UnicodeEncoder;

public class TabularEvaluation {
	String newline2 = System.getProperty("line.separator");
	StringBuilder steps;
	private int[] minterms;
	private int[] donCares;
	private int[] realMinterms;
	private int numberOfVariables;
	private SLinkedList finalPrimeImplicants;
	public char[] literals = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };


	public TabularEvaluation() {
		dcChecker = 1;
	}
	
	public void setRealMinterms(int[] realMinterms) {
		this.realMinterms = realMinterms;

	}

	public void setParameters(int[] minterms, int numberOfVariables, int[] dont) {
		steps = null;
		steps = new StringBuilder();
		this.donCares = dont;
		this.minterms = minterms;
		this.numberOfVariables = numberOfVariables;
		this.finalPrimeImplicants = new SLinkedList();
	}

	public SLinkedList findPrimeImplicants() {
		Arrays.sort(this.minterms);

		removeDuplicates();

		SLinkedList[] startingGroup = groupTheMinterms();
		this.fillFinalArray(startingGroup);
		return this.finalPrimeImplicants;
	}

	private void removeDuplicates() {
		int length = minterms.length;
		int j = 1;
		int[] tmpArray = new int[length];
		tmpArray[0] = minterms[0];
		for (int i = 1; i < length; i++) {
			if (minterms[i] == minterms[i - 1]) {
				continue;
			} else {
				tmpArray[j] = minterms[i];
				j++;
			}
		}
		minterms = Arrays.copyOfRange(tmpArray, 0, j);
	}

	private SLinkedList[] groupTheMinterms() {
		SLinkedList[] grouping = new SLinkedList[this.numberOfVariables + 1];
		for (int i = 0; i <= this.numberOfVariables; i++) {
			grouping[i] = new SLinkedList();
		}

		int length = this.minterms.length;
		for (int i = 0; i < length; i++) {
			GroupElements tmp = new GroupElements();
			tmp.value = minterms[i];
			grouping[Integer.bitCount(minterms[i])].add(tmp);
		}

		return grouping;
	}

	int lvl = 1;
	int dcChecker;

	private void fillFinalArray(SLinkedList[] groupsArray) {
		int length = groupsArray.length;

		if (length == 1) {
			int fi = groupsArray[0].size();
			for (int i = 1; i < groupsArray.length ; i++) {
				fi += groupsArray[i].size();
			}
			if (fi > 0)
			steps.append(newline2 + newline2 + "========================"+newline2+"LEVEL " + (lvl++) + newline2);

			for (int i = 0; i < groupsArray.length; i++) {
				if ( groupsArray[i].size() > 0  )
				steps.append(newline2 + "Group of : " + i + newline2);

				for (int j = 0; j < groupsArray[i].size(); j++) {
					steps.append( ((GroupElements) groupsArray[i].get(j)).value);
					if (((GroupElements) groupsArray[i].get(j)).arraySize > 0){
						steps.append("( ");
					}
//						else {
//						System.out.print("\n");
//					}
					boolean checked = false;
					for (int j2 = 0; j2 < ((GroupElements) groupsArray[i].get(j)).arraySize; j2++) {
						steps.append(((GroupElements) groupsArray[i].get(j)).Array[j2] + " ");
						checked = true;
					}
					if (checked){
						steps.append(")");
					}
					if (((GroupElements) groupsArray[i].get(j)).visited) {
						steps.append("\u2713");

//						try {
//						PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
//						writer.println("\u001B[1m \u2713 \u001B[1m");
//						} catch (Exception e) {
//							// nothing
//						}
					} else {
						steps.append(" *");
					}
					steps.append(" " + newline2);

				}
			}
			SNode tmpo = groupsArray[0].getHead();

			while (tmpo != null) {

				this.finalPrimeImplicants.add(tmpo.getValue());
				tmpo = tmpo.getNext();
			}
			return;
		}

		SLinkedList[] grouping = new SLinkedList[length - 1];

		for (int i = 0; i < length - 1; i++) {
			grouping[i] = new SLinkedList();
		}

		int gr = 0;

		for (int i = 0; i < length - 1; i++) {

			SNode tmp1 = groupsArray[i].getHead();
			SNode tmp2 = groupsArray[i + 1].getHead();

			int el = 1;

			while (tmp1 != null) {

				int visit = 0;
				while (tmp2 != null) {

					if (((GroupElements) tmp1.getValue()).arraySize == ((GroupElements) tmp2.getValue()).arraySize) {
						int val1 = ((GroupElements) tmp1.getValue()).value;
						int val2 = ((GroupElements) tmp2.getValue()).value;

						if (val2 < val1) {

							tmp2 = tmp2.getNext();
							continue;
						} else {

							int val3 = val1 ^ val2;
							if ((val3 & (val3 - 1)) == 0) {
								if (this.equality(((GroupElements) tmp1.getValue()).Array,
										((GroupElements) tmp2.getValue()).Array)) {

									visit++;
									int diff = val2 - val1;

									GroupElements tmp = new GroupElements();
									tmp.value = ((GroupElements) tmp1.getValue()).value;

									tmp.arraySize = (((GroupElements) tmp1.getValue()).arraySize) + 1;

									int[] tmpArray = new int[(((GroupElements) tmp1.getValue()).arraySize) + 1];
									this.addToSortedArray(((GroupElements) tmp1.getValue()).Array, diff, tmpArray);
									tmp.Array = tmpArray;

									if (!this.Found(tmp, grouping[i])) {
										grouping[i].add(tmp);
									}
									((GroupElements) tmp1.getValue()).visited = true;
									((GroupElements) tmp2.getValue()).visited = true;
								}
							}
							tmp2 = tmp2.getNext();

						}
					}
				}
				if (visit == 0 && ((GroupElements) tmp1.getValue()).visited == false) {
					boolean dc = false;
					// System.out.println("in hnaaaaaaaaaa");
					if (this.donCares != null && dcChecker == 1) {
						for (int u = 0; u < this.donCares.length; u++) {

							if (((GroupElements) tmp1.getValue()).value == this.donCares[u]) {
								dc = true;
								this.minterms = this.removeFromArray(this.minterms,
										((GroupElements) tmp1.getValue()).value);
								break;
							}
						}
					}
					if (!dc) {
						this.finalPrimeImplicants.add(((GroupElements) tmp1.getValue()));
					}
				}
				tmp2 = groupsArray[i + 1].getHead();
				tmp1 = tmp1.getNext();
			}
		}

		SNode tmp1 = groupsArray[length - 1].getHead();
		while (tmp1 != null) {
			if (!(((GroupElements) tmp1.getValue()).visited)) {
				boolean dc = false;

				if ( this.donCares != null) {
					if (dcChecker == 1) {
						for (int u = 0; u < this.donCares.length; u++) {
	
							if (((GroupElements) tmp1.getValue()).value == this.donCares[u]) {
								dc = true;
								this.minterms = this.removeFromArray(this.minterms,
										((GroupElements) tmp1.getValue()).value);
								break;
							}
						}
					}
				}

				if (!dc) {
					this.finalPrimeImplicants.add(tmp1.getValue());
				}
			}
			tmp1 = tmp1.getNext();
		}
		
		int fi = groupsArray[0].size();
		for (int i = 1; i < groupsArray.length ; i++) {
			fi += groupsArray[i].size();
		}
		if (fi > 0)
		steps.append(newline2 + newline2 + "========================"+newline2+"LEVEL " + (lvl++) + newline2);

		for (int i = 0; i < groupsArray.length; i++) {
			if ( groupsArray[i].size() > 0  )
			steps.append(newline2 + "Group of : " + i + newline2);

			for (int j = 0; j < groupsArray[i].size(); j++) {
				steps.append( ((GroupElements) groupsArray[i].get(j)).value);
				if (((GroupElements) groupsArray[i].get(j)).arraySize > 0){
					steps.append("( ");
				}
//					else {
//					System.out.print("\n");
//				}
				boolean checked = false;
				for (int j2 = 0; j2 < ((GroupElements) groupsArray[i].get(j)).arraySize; j2++) {
					steps.append(((GroupElements) groupsArray[i].get(j)).Array[j2] + " ");
					checked = true;
				}
				if (checked){
					steps.append(")");
				}
				if (((GroupElements) groupsArray[i].get(j)).visited) {
					steps.append("\u2713");

//					try {
//					PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
//					writer.println("\u001B[1m \u2713 \u001B[1m");
//					} catch (Exception e) {
//						// nothing
//					}
				} else {
					steps.append(" *");
				}
				steps.append(" " + newline2);

			}
		}

		dcChecker++;
		fillFinalArray(grouping);
	}

	private boolean equality(int[] array1, int[] array2) {
		if (array1 == null) {
			return true;
		} else {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] != array2[i]) {
					return false;
				}
			}
			return true;
		}
	}

	private void addToSortedArray(int[] array, int value, int[] finalArray) {
		int length;
		if (array == null) {
			length = 0;
		} else {
			length = array.length;
		}
		for (int i = 0; i < length; i++) {
			finalArray[i] = array[i];
		}
		finalArray[length] = value;
		Arrays.sort(finalArray);
	}

	private boolean Found(GroupElements value, SLinkedList list) {
		SNode tmp = list.getHead();
		while (tmp != null) {
			if (value.value == ((GroupElements) tmp.getValue()).value) {
				if (value.arraySize == ((GroupElements) tmp.getValue()).arraySize) {
					int length = value.arraySize;
					int i;
					// System.out.println(value.arraySize + " hna " +
					// value.Array.length);
					for (i = 0; i < length; i++) {
						if (value.Array[i] != ((GroupElements) tmp.getValue()).Array[i]) {
							break;
						}
					}
					if (i == length) {
						return true;
					}
				}
			}
			tmp = tmp.getNext();
		}
		return false;
	}

	public int[] setMinterms(String readMin, int numOFV) {
		String[] mintermsS;
		int[] minterms;
		if (readMin != null) {
			mintermsS = readMin.split("\\s*,\\s*");
			minterms = new int[mintermsS.length];
			for (int i = 0; i < minterms.length; i++) {
				try {
					minterms[i] = Integer.parseInt(mintermsS[i]);
				} catch (Exception e) {
					throw new RuntimeException("Enter integer values as minterms!");
				}
				if (minterms[i] >= Math.pow(2, numOFV)) {
					throw new RuntimeException("Minterm Must be smaller than!" + Math.pow(2, numOFV));
				}
			}
		} else {
			throw new RuntimeException("Empty input!");
		}

		return minterms;

	}

	public int[] setDontCares(String readDC, int numOFV) {
		String[] dontCaresS;
		int[] dontCares;
		if (readDC != null && readDC.length() != 0) {
			dontCaresS = readDC.split("\\s*,\\s*");
			dontCares = new int[dontCaresS.length];
			for (int i = 0; i < dontCaresS.length; i++) {
				try {
					dontCares[i] = Integer.parseInt(dontCaresS[i]);

				} catch (Exception e) {
					throw new RuntimeException("Enter integer values as minterms!");
				}
				if (dontCares[i] >= Math.pow(2, numOFV)) {
					throw new RuntimeException("Dont care term Must be smaller than!" + Math.pow(2, numOFV));
				}
			}

		} else {
			throw new RuntimeException("Empty input!");
		}
		return dontCares;

	}

	public boolean isValidInput(int[] minterms, int[] dontCares) {
		if (dontCares == null)
			return true;
		for (int j = 0; j < dontCares.length; j++) {
			for (int k = 0; k < minterms.length; k++) {
				if (dontCares[j] == minterms[k]) {
					return false;
				}
			}
		}
		return true;
	}

	public void printTest(SLinkedList temp) {
		System.out.println("Num of mins : " + temp.size());

		for (int i = 0; i < temp.size(); i++) {
			// System.out.print(((GroupElements) temp.get(i)).value + "hna ");
			for (int j = 0; j < ((GroupElements) temp.get(i)).arraySize; j++) {
				System.out.print(((GroupElements) temp.get(i)).Array[j] + " ");
			}
			System.out.println(" ");
		}
	}

	public String mapPrimeImplicants(SLinkedList res) {
//		System.out.println("=========== > NEW PARTITION < ===========");
//		System.out.println("size : " + res.size());
		int completeChecker = 0;
		StringBuilder primeImlicants = new StringBuilder();

		for (int i = 0; i < res.size(); i++) {
			StringBuilder singlePrime = new StringBuilder();
			int starterI = 0 ^ ((GroupElements) res.get(i)).value;
			int ignoredSlots = 0;
			for (int j = 0; j < ((GroupElements) res.get(i)).arraySize; j++) {
				ignoredSlots ^= ((GroupElements) res.get(i)).Array[j];
			}
			for (double power = 0; power < this.numberOfVariables; power++) {
				int digit = (int) Math.pow(2, power);
				if ((digit & ignoredSlots) == 0) {
					completeChecker++;
					if ((digit & starterI) > 0) {
						singlePrime.insert(0, this.literals[(this.numberOfVariables) - 1 - (int) power]);
					} else {
						singlePrime.insert(0, this.literals[(this.numberOfVariables) - 1 - (int) power] + "'");
					}
				} else {
					continue;
				}
			}
			primeImlicants.append(singlePrime + " , ");
		}

		if (completeChecker == 0)
			return "1";

		return (primeImlicants.toString()).substring(0, primeImlicants.toString().length() - 3);
	}

	public SLinkedList rowColumnChart(SLinkedList res) {
		if (res.size() != 0) {
			int[] realmin = this.realMinterms;

			SLinkedList[] chart = new SLinkedList[res.size()];

			for (int primeIndex = 0; primeIndex < res.size(); primeIndex++) {
				chart[primeIndex] = getPowerSet(((GroupElements) res.get(primeIndex)).Array,
						((GroupElements) res.get(primeIndex)).value);
			}
//			System.out.print("   ");
//
//			for (int i = 0; i < this.minterms.length ; i++) {
//				System.out.print("m" + (i +1) +"  ");
//			}
//			System.out.println();
//			for (int i = 0; i < chart.length; i++) {
//				for (int j = 0; j < chart[i].size() ; j++) {
//					//System.out.println(chart[i].get(j));
//					if (j == 0)
//					System.out.printf("%" + (((int)chart[i].get(j) * 5) + 4  ) + "s", "X");
//					else
//						System.out.printf("%" + ( (  (  (int)chart[i].get(j))*5 -   (  (int)chart[i].get(j-1)) * 5  )  )+ "s", "X");		
//				}
//				System.out.println("\n");
//			}

			return petriks(chart, realmin);
		}
		return null;
	}

	private SLinkedList getPowerSet(int[] diff, int valueoo) {
		if (diff == null) {
			SLinkedList spec = new SLinkedList();
			spec.add(valueoo);
			return spec;
		}
		int numOfCoveredMinterms = (int) Math.pow(2, diff.length);

		SLinkedList CoveredMintermsByOnePrime = new SLinkedList();
		for (int i = 0; i < numOfCoveredMinterms; i++) {

			int possiblePrime = 0;
			for (int j = 0; j < diff.length; j++) {
				int digit = (int) Math.pow(2, j);
				if ((digit & i) > 0)
					possiblePrime += diff[j];
			}
			CoveredMintermsByOnePrime.add(possiblePrime + valueoo);
		}
		return CoveredMintermsByOnePrime;
	}

	public SLinkedList petriks(SLinkedList[] chart, int[] minterms) {

		minterms = removeDuplicatesForGivenArray(minterms);

		SLinkedList pet = new SLinkedList();
		for (int i = 0; i < minterms.length; i++) {
			pet.add(new petrikGroup());
			((petrikGroup) pet.get(i)).minterm = minterms[i];
		}

		for (int t = 0; t < chart.length; t++) {
			for (int d = 0; d < chart[t].size(); d++) {
				for (int min = 0; min < pet.size(); min++) {
					if ((int) chart[t].get(d) == ((petrikGroup) pet.get(min)).minterm) {
						((petrikGroup) pet.get(min)).primeImplicant.add((int) Math.pow(2, t));
						break;
					}
				}
			}
		}

		return multiplyOut(pet);

	}

	public SLinkedList multiplyOut(SLinkedList pet) {

		while (pet.size() > 1) {

			SLinkedList tmp1 = new SLinkedList();
			tmp1 = ((petrikGroup) pet.get(0)).primeImplicant;

			SLinkedList tmp2 = new SLinkedList();
			tmp2 = ((petrikGroup) pet.get(1)).primeImplicant;

			pet.remove(0);
			pet.remove(0);

			SLinkedList ored = new SLinkedList();

			for (int i = 0; i < tmp1.size(); i++) {
				for (int j = 0; j < tmp2.size(); j++) {
					ored.add(((int) tmp1.get(i) | (int) tmp2.get(j)));
				}
			}

			for (int i1 = 0; i1 < ored.size() - 1; i1++) {
				for (int j1 = i1 + 1; j1 < ored.size(); j1++) {
					if (((int) ored.get(i1) & (int) ored.get(j1)) == (int) ored.get(i1)) {
						ored.remove(j1);
						j1--;

					} else if (((int) ored.get(i1) & (int) ored.get(j1)) == (int) ored.get(j1)) {
						ored.remove(i1);
						i1--;
						break;
					}
				}
			}

			petrikGroup tempRes = new petrikGroup();
			tempRes.primeImplicant = ored;

			pet.add(tempRes);

		}

		return pet;
	}

	private int[] removeDuplicatesForGivenArray(int[] given) {
		
		int length = given.length;
		int j = 1;
		int[] tmpArray = new int[length];
		tmpArray[0] = given[0];
		for (int i = 1; i < length; i++) {
			if (given[i] == given[i - 1]) {
				continue;
			} else {
				tmpArray[j] = given[i];
				j++;
			}
		}
		given = Arrays.copyOfRange(tmpArray, 0, j);
		return given;
	}

	public String[] mapAllSolutions(SLinkedList pet, String[] allPrimeImplicants) {
		String[] allSolutions = new String[((petrikGroup) pet.get(0)).primeImplicant.size()];
		for (int i = 0; i < ((petrikGroup) pet.get(0)).primeImplicant.size(); i++) {

			StringBuilder temp = new StringBuilder();
			int tmpInt = (int) ((petrikGroup) pet.get(0)).primeImplicant.get(i);

			while (tmpInt > 0) {
				int bit = tmpInt ^ (tmpInt - 1);
				bit = Integer.bitCount(bit);
				tmpInt = tmpInt & (tmpInt - 1);
				temp.append(allPrimeImplicants[bit - 1] + " + ");
			}

			allSolutions[i] = temp.toString().substring(0, temp.length() - 3);
		}
		return allSolutions;
	}

	private int[] removeFromArray(int[] temp, int value) {
		int[] corrected = new int[temp.length - 1];
		int index = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] != value) {
				corrected[index] = temp[i];
				index++;
			}
		}
		return corrected;
	}

	public SLinkedList getMinSolutions(SLinkedList pet) {
		SLinkedList temp = new SLinkedList();
		temp.add(((petrikGroup) pet.get(0)).primeImplicant.get(0));
		for (int i = 1; i < ((petrikGroup) pet.get(0)).primeImplicant.size(); i++) {
			if (Integer.bitCount((int) ((petrikGroup) pet.get(0)).primeImplicant.get(i)) == Integer
					.bitCount((int) temp.get(0))) {
				temp.add(((petrikGroup) pet.get(0)).primeImplicant.get(i));
			} else if (Integer.bitCount((int) ((petrikGroup) pet.get(0)).primeImplicant.get(i)) < Integer
					.bitCount((int) temp.get(0))) {
				temp.clear();
				temp.add(((petrikGroup) pet.get(0)).primeImplicant.get(i));
			}
		}
		petrikGroup tempo = new petrikGroup();
		tempo.primeImplicant = temp;
		SLinkedList fin = new SLinkedList();
		fin.add(tempo);
		return fin;
	}
	
	public String getSteps() {
		return steps.toString();
	}

	// String[] allPrimeImplicants = mapped.split("\\s*,\\s*");

}