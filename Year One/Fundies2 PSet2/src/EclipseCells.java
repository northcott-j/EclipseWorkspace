import tester.Tester;

interface IData {
	
}

class Number implements IData {
	int val;
	w
	Number(int val) {
		this.val = val;
	}
}

class Cell {
	String column;
	int row;
	IData data;
	
	Cell(String column, int row, IData data) {
		this.column = column;
		this.row = row;
		this.data = data;
	}
}

class Formula implements IData {
	Cell cell1;
	String function;
	Cell cell2;
	
	Formula(Cell cell1, String function, Cell cell2) {
		this.cell1 = cell1;
		this.function = function;
		this.cell2 = cell2;
	}
}

class ExamplesExcelCells {
	/*
Cell equations: 	 
  |      A     |      B     |      C     |      D     |      E     |
--+------------+------------+------------+------------+------------+
1 |     13     |      5     |      2     |      3     |      7     |
--+------------+------------+------------+------------+------------+
2 |            | sub(A3,C1) |            | mod(B2,E2) | sub(E1,D1) |
--+------------+------------+------------+------------+------------+
3 | mul(A1,B1) | mod(E1,A3) |            | mul(D2,A1) |            |
--+------------+------------+------------+------------+------------+
4 |            | sub(C1,A3) | mul(E1,D1) | sub(C4,A1) | mul(D1,A3) |
--+------------+------------+------------+------------+------------+
5 | mod(D3,C5) |            | sub(D4,B3) | mod(A1,D1) |            |
--+------------+------------+------------+------------+------------+
	  Added:
	  B4, D5, E4
	  
Cell Values:
  |      A     |      B     |      C     |      D     |      E     |
--+------------+------------+------------+------------+------------+
1 |     13     |      5     |      2     |      3     |      7     |
--+------------+------------+------------+------------+------------+
2 |            |     60     |            |      0     |      4     |
--+------------+------------+------------+------------+------------+
3 |     65     |      7     |            |      0     |            |
--+------------+------------+------------+------------+------------+
4 |            |     -63    |     21     |      8     |     195    |
--+------------+------------+------------+------------+------------+
5 |      0     |            |      8     |      1     |            |
--+------------+------------+------------+------------+------------+
	 */
	
	Cell cellA1 = new Cell("A", 1, new Number(13));
	Cell cellB1 = new Cell("B", 1, new Number(5));
	Cell cellC1 = new Cell("C", 1, new Number(2));
	Cell cellD1 = new Cell("D", 1, new Number(3));
	Cell cellE1 = new Cell("E", 1, new Number(7));
}