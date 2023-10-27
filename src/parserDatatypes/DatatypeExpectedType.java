package parserDatatypes;

public enum DatatypeExpectedType {
	DATA_TYPE_EXPECT_PRIMITIVE,
	DATA_TYPE_EXPECT_UNION,
	DATA_TYPE_EXPECT_STRUCT,
	// Class wird in diesem Compiler als gleicher Typ wie struct behandelt, wir merken uns trotzdem die Eingabe.
	DATA_TYPE_EXPECT_CLASS
}
