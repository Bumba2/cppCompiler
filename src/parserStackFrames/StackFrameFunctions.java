package parserStackFrames;

import lexer.SymbolTable;
import parser.Node;
import parserDatatypes.Function;

public class StackFrameFunctions {
	public static int STACK_PUSH_SIZE = 4;
	
	public static void stackFramePop(Node<Object> funcNode) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		StackFrame<Object> frame = func.getFrame();
		frame.getElements().vectorPop();
	}
	
	public static StackFrameElement stackFrameBack(Node<Object> funcNode) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		return (StackFrameElement) func.getFrame().getElements().vectorBackOrNull();
	}
	
	public static StackFrameElement stackFrameBackExpect(Node<Object> funcNode, StackFrameElementType expectingType, String expectingName) {
		StackFrameElement element = StackFrameFunctions.stackFrameBack(funcNode);
		if (element != null && element.getType() != expectingType || !(SymbolTable.S_EQ(element.getName(), expectingName))) {
			return null;
		}
		return element;
	}
	
	public static void stackFramePopExisting(Node<Object> funcNode, StackFrameElementType expectingType, String expectingName) {
		StackFrameElement lastElement = StackFrameFunctions.stackFrameBack(funcNode);
		assert(lastElement != null);
		assert(lastElement.getType() == expectingType && SymbolTable.S_EQ(lastElement.getName(), expectingName));
		StackFrameFunctions.stackFramePop(funcNode);
	}
	
	public static void stackFramePeekStart(Node<Object> funcNode) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		StackFrame<Object> frame = func.getFrame();
		frame.getElements().vectorSetPeekPointerEnd();
	}
	
	public static StackFrameElement stackFramePeek(Node<Object> funcNode) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		StackFrame<Object> frame = func.getFrame();
		return (StackFrameElement) frame.getElements().vectorPeek();
	}
	
	public static void stackFramePush(Node<Object> funcNode, StackFrameElement element) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		StackFrame<Object> frame = func.getFrame();
		// Der Stack wächst nach unten (daher negative Zahlen).
		element.setOffsetFromBp(-(frame.getElements().vectorCount() * StackFrameFunctions.STACK_PUSH_SIZE));
		frame.getElements().vectorPush(element);
	}
	
	public static void stackFrameSub(Node<Object> funcNode, StackFrameElementType type, String name, int amount) {
		assert((amount % StackFrameFunctions.STACK_PUSH_SIZE) == 0);
		int totalPushes = amount / StackFrameFunctions.STACK_PUSH_SIZE;
		for(int i = 0; i < totalPushes; i++) {
			StackFrameFunctions.stackFramePush(funcNode, new StackFrameElement(type, name));
		}
	}
	
	public static void stackFrameAdd(Node<Object> funcNode, StackFrameElementType type, String name, int amount) {
		assert((amount % StackFrameFunctions.STACK_PUSH_SIZE) == 0);
		int totalPushes = amount / StackFrameFunctions.STACK_PUSH_SIZE;
		for(int i = 0; i < totalPushes; i++) {
			StackFrameFunctions.stackFramePop(funcNode);
		}
	}
	
	public static void stackFrameAssertEmpty(Node<Object> funcNode) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		StackFrame<Object> frame = func.getFrame();
		assert(frame.getElements().vectorCount() == 0);
	}
}