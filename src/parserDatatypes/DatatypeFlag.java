package parserDatatypes;

public class DatatypeFlag {
	public static int DATATYPE_FLAG_IS_SIGNED = 0b00000001;
	public static int DATATYPE_FLAG_IS_STATIC = 0b00000010;
	public static int DATATYPE_FLAG_IS_CONST= 0b00000100;
	public static int DATATYPE_FLAG_IS_POINTER = 0b00001000;
	public static int DATATYPE_FLAG_IS_ARRAY = 0b00010000;
	public static int DATATYPE_FLAG_IS_EXTERN = 0b00100000;
	public static int DATATYPE_FLAG_IS_RESTRICT = 0b01000000;
	public static int DATATYPE_FLAG_IGNORE_TYPE_CHECKING = 0b10000000;
	public static int DATATYPE_FLAG_IS_SECONDARY = 0b100000000;
	public static int DATATYPE_FLAG_STRUCT_UNION_NO_NAME = 0b1000000000;
	public static int DATATYPE_FLAG_IS_LITERAL = 0b10000000000;
}