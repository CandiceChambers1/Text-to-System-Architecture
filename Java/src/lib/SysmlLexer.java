// Generated from .\Sysml.g4 by ANTLR 4.9.2

package lib;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SysmlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		With=1, Comma=2, And=3, Form=4, End=5, Colon=6, It=7, To=8, Them=9, From=10, 
		Struct_verb=11, Connect_verb=12, Instantiates=13, Internal=14, Port=15, 
		Func_verb=16, Energy=17, Adj_value=18, Struct_noun=19, State=20, Signal=21, 
		WS=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"With", "Comma", "And", "Form", "End", "Colon", "It", "To", "Them", "From", 
			"Struct_verb", "Connect_verb", "Instantiates", "Internal", "Port", "Func_verb", 
			"Energy", "Adj_value", "Struct_noun", "State", "Signal", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'with'", "','", "'and'", "'form'", "'.'", "':'", "'it'", "'to'", 
			"'them'", "'from'", "'consists'", "'connected'", "'instantiates'", "'internal_components'", 
			"'port_components'", null, "'energy'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "With", "Comma", "And", "Form", "End", "Colon", "It", "To", "Them", 
			"From", "Struct_verb", "Connect_verb", "Instantiates", "Internal", "Port", 
			"Func_verb", "Energy", "Adj_value", "Struct_noun", "State", "Signal", 
			"WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SysmlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Sysml.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u013e\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\3"+
		"\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b"+
		"\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\5\21\u0114\n\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\6\23\u011f\n\23\r\23\16\23\u0120\3\24\6\24\u0124"+
		"\n\24\r\24\16\24\u0125\3\24\7\24\u0129\n\24\f\24\16\24\u012c\13\24\3\25"+
		"\6\25\u012f\n\25\r\25\16\25\u0130\3\26\6\26\u0134\n\26\r\26\16\26\u0135"+
		"\3\27\6\27\u0139\n\27\r\27\16\27\u013a\3\27\3\27\2\2\30\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30\3\2\7\3\2C\\\3\2c|\5\2\62;C\\aa\6\2\62;C\\aac|\5"+
		"\2\13\f\16\17\"\"\2\u0152\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2"+
		"\2\2\2-\3\2\2\2\3/\3\2\2\2\5\64\3\2\2\2\7\66\3\2\2\2\t:\3\2\2\2\13?\3"+
		"\2\2\2\rA\3\2\2\2\17C\3\2\2\2\21F\3\2\2\2\23I\3\2\2\2\25N\3\2\2\2\27S"+
		"\3\2\2\2\31\\\3\2\2\2\33f\3\2\2\2\35s\3\2\2\2\37\u0087\3\2\2\2!\u0113"+
		"\3\2\2\2#\u0115\3\2\2\2%\u011c\3\2\2\2\'\u0123\3\2\2\2)\u012e\3\2\2\2"+
		"+\u0133\3\2\2\2-\u0138\3\2\2\2/\60\7y\2\2\60\61\7k\2\2\61\62\7v\2\2\62"+
		"\63\7j\2\2\63\4\3\2\2\2\64\65\7.\2\2\65\6\3\2\2\2\66\67\7c\2\2\678\7p"+
		"\2\289\7f\2\29\b\3\2\2\2:;\7h\2\2;<\7q\2\2<=\7t\2\2=>\7o\2\2>\n\3\2\2"+
		"\2?@\7\60\2\2@\f\3\2\2\2AB\7<\2\2B\16\3\2\2\2CD\7k\2\2DE\7v\2\2E\20\3"+
		"\2\2\2FG\7v\2\2GH\7q\2\2H\22\3\2\2\2IJ\7v\2\2JK\7j\2\2KL\7g\2\2LM\7o\2"+
		"\2M\24\3\2\2\2NO\7h\2\2OP\7t\2\2PQ\7q\2\2QR\7o\2\2R\26\3\2\2\2ST\7e\2"+
		"\2TU\7q\2\2UV\7p\2\2VW\7u\2\2WX\7k\2\2XY\7u\2\2YZ\7v\2\2Z[\7u\2\2[\30"+
		"\3\2\2\2\\]\7e\2\2]^\7q\2\2^_\7p\2\2_`\7p\2\2`a\7g\2\2ab\7e\2\2bc\7v\2"+
		"\2cd\7g\2\2de\7f\2\2e\32\3\2\2\2fg\7k\2\2gh\7p\2\2hi\7u\2\2ij\7v\2\2j"+
		"k\7c\2\2kl\7p\2\2lm\7v\2\2mn\7k\2\2no\7c\2\2op\7v\2\2pq\7g\2\2qr\7u\2"+
		"\2r\34\3\2\2\2st\7k\2\2tu\7p\2\2uv\7v\2\2vw\7g\2\2wx\7t\2\2xy\7p\2\2y"+
		"z\7c\2\2z{\7n\2\2{|\7a\2\2|}\7e\2\2}~\7q\2\2~\177\7o\2\2\177\u0080\7r"+
		"\2\2\u0080\u0081\7q\2\2\u0081\u0082\7p\2\2\u0082\u0083\7g\2\2\u0083\u0084"+
		"\7p\2\2\u0084\u0085\7v\2\2\u0085\u0086\7u\2\2\u0086\36\3\2\2\2\u0087\u0088"+
		"\7r\2\2\u0088\u0089\7q\2\2\u0089\u008a\7t\2\2\u008a\u008b\7v\2\2\u008b"+
		"\u008c\7a\2\2\u008c\u008d\7e\2\2\u008d\u008e\7q\2\2\u008e\u008f\7o\2\2"+
		"\u008f\u0090\7r\2\2\u0090\u0091\7q\2\2\u0091\u0092\7p\2\2\u0092\u0093"+
		"\7g\2\2\u0093\u0094\7p\2\2\u0094\u0095\7v\2\2\u0095\u0096\7u\2\2\u0096"+
		" \3\2\2\2\u0097\u0098\7k\2\2\u0098\u0099\7o\2\2\u0099\u009a\7r\2\2\u009a"+
		"\u009b\7q\2\2\u009b\u009c\7t\2\2\u009c\u009d\7v\2\2\u009d\u0114\7u\2\2"+
		"\u009e\u009f\7g\2\2\u009f\u00a0\7z\2\2\u00a0\u00a1\7r\2\2\u00a1\u00a2"+
		"\7q\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4\7v\2\2\u00a4\u0114\7u\2\2\u00a5"+
		"\u00a6\7v\2\2\u00a6\u00a7\7t\2\2\u00a7\u00a8\7c\2\2\u00a8\u00a9\7p\2\2"+
		"\u00a9\u00aa\7u\2\2\u00aa\u00ab\7h\2\2\u00ab\u00ac\7g\2\2\u00ac\u00ad"+
		"\7t\2\2\u00ad\u0114\7u\2\2\u00ae\u00af\7i\2\2\u00af\u00b0\7w\2\2\u00b0"+
		"\u00b1\7k\2\2\u00b1\u00b2\7f\2\2\u00b2\u00b3\7g\2\2\u00b3\u0114\7u\2\2"+
		"\u00b4\u00b5\7u\2\2\u00b5\u00b6\7w\2\2\u00b6\u00b7\7r\2\2\u00b7\u00b8"+
		"\7r\2\2\u00b8\u00b9\7n\2\2\u00b9\u00ba\7k\2\2\u00ba\u00bb\7g\2\2\u00bb"+
		"\u0114\7u\2\2\u00bc\u00bd\7e\2\2\u00bd\u00be\7q\2\2\u00be\u00bf\7p\2\2"+
		"\u00bf\u00c0\7x\2\2\u00c0\u00c1\7g\2\2\u00c1\u00c2\7t\2\2\u00c2\u00c3"+
		"\7v\2\2\u00c3\u0114\7u\2\2\u00c4\u00c5\7o\2\2\u00c5\u00c6\7k\2\2\u00c6"+
		"\u00c7\7z\2\2\u00c7\u00c8\7g\2\2\u00c8\u0114\7u\2\2\u00c9\u00ca\7e\2\2"+
		"\u00ca\u00cb\7q\2\2\u00cb\u00cc\7w\2\2\u00cc\u00cd\7r\2\2\u00cd\u00ce"+
		"\7n\2\2\u00ce\u00cf\7g\2\2\u00cf\u0114\7u\2\2\u00d0\u00d1\7u\2\2\u00d1"+
		"\u00d2\7g\2\2\u00d2\u00d3\7r\2\2\u00d3\u00d4\7c\2\2\u00d4\u00d5\7t\2\2"+
		"\u00d5\u00d6\7c\2\2\u00d6\u00d7\7v\2\2\u00d7\u00d8\7g\2\2\u00d8\u0114"+
		"\7u\2\2\u00d9\u00da\7g\2\2\u00da\u00db\7p\2\2\u00db\u00dc\7g\2\2\u00dc"+
		"\u00dd\7t\2\2\u00dd\u00de\7i\2\2\u00de\u00df\7k\2\2\u00df\u00e0\7|\2\2"+
		"\u00e0\u00e1\7g\2\2\u00e1\u0114\7u\2\2\u00e2\u00e3\7f\2\2\u00e3\u00e4"+
		"\7g\2\2\u00e4\u00e5\7g\2\2\u00e5\u00e6\7p\2\2\u00e6\u00e7\7g\2\2\u00e7"+
		"\u00e8\7t\2\2\u00e8\u00e9\7i\2\2\u00e9\u00ea\7k\2\2\u00ea\u00eb\7|\2\2"+
		"\u00eb\u00ec\7g\2\2\u00ec\u0114\7u\2\2\u00ed\u00ee\7u\2\2\u00ee\u00ef"+
		"\7v\2\2\u00ef\u00f0\7q\2\2\u00f0\u00f1\7t\2\2\u00f1\u00f2\7g\2\2\u00f2"+
		"\u0114\7u\2\2\u00f3\u00f4\7u\2\2\u00f4\u00f5\7v\2\2\u00f5\u00f6\7q\2\2"+
		"\u00f6\u00f7\7r\2\2\u00f7\u0114\7u\2\2\u00f8\u00f9\7e\2\2\u00f9\u00fa"+
		"\7j\2\2\u00fa\u00fb\7c\2\2\u00fb\u00fc\7p\2\2\u00fc\u00fd\7i\2\2\u00fd"+
		"\u00fe\7g\2\2\u00fe\u0114\7u\2\2\u00ff\u0100\7t\2\2\u0100\u0101\7g\2\2"+
		"\u0101\u0102\7i\2\2\u0102\u0103\7w\2\2\u0103\u0104\7n\2\2\u0104\u0105"+
		"\7c\2\2\u0105\u0106\7v\2\2\u0106\u0107\7g\2\2\u0107\u0114\7u\2\2\u0108"+
		"\u0109\7f\2\2\u0109\u010a\7k\2\2\u010a\u010b\7u\2\2\u010b\u010c\7v\2\2"+
		"\u010c\u010d\7t\2\2\u010d\u010e\7k\2\2\u010e\u010f\7d\2\2\u010f\u0110"+
		"\7w\2\2\u0110\u0111\7v\2\2\u0111\u0112\7g\2\2\u0112\u0114\7u\2\2\u0113"+
		"\u0097\3\2\2\2\u0113\u009e\3\2\2\2\u0113\u00a5\3\2\2\2\u0113\u00ae\3\2"+
		"\2\2\u0113\u00b4\3\2\2\2\u0113\u00bc\3\2\2\2\u0113\u00c4\3\2\2\2\u0113"+
		"\u00c9\3\2\2\2\u0113\u00d0\3\2\2\2\u0113\u00d9\3\2\2\2\u0113\u00e2\3\2"+
		"\2\2\u0113\u00ed\3\2\2\2\u0113\u00f3\3\2\2\2\u0113\u00f8\3\2\2\2\u0113"+
		"\u00ff\3\2\2\2\u0113\u0108\3\2\2\2\u0114\"\3\2\2\2\u0115\u0116\7g\2\2"+
		"\u0116\u0117\7p\2\2\u0117\u0118\7g\2\2\u0118\u0119\7t\2\2\u0119\u011a"+
		"\7i\2\2\u011a\u011b\7{\2\2\u011b$\3\2\2\2\u011c\u011e\t\2\2\2\u011d\u011f"+
		"\t\3\2\2\u011e\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u011e\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121&\3\2\2\2\u0122\u0124\t\2\2\2\u0123\u0122\3\2\2\2"+
		"\u0124\u0125\3\2\2\2\u0125\u0123\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u012a"+
		"\3\2\2\2\u0127\u0129\t\4\2\2\u0128\u0127\3\2\2\2\u0129\u012c\3\2\2\2\u012a"+
		"\u0128\3\2\2\2\u012a\u012b\3\2\2\2\u012b(\3\2\2\2\u012c\u012a\3\2\2\2"+
		"\u012d\u012f\t\3\2\2\u012e\u012d\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u012e"+
		"\3\2\2\2\u0130\u0131\3\2\2\2\u0131*\3\2\2\2\u0132\u0134\t\5\2\2\u0133"+
		"\u0132\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0133\3\2\2\2\u0135\u0136\3\2"+
		"\2\2\u0136,\3\2\2\2\u0137\u0139\t\6\2\2\u0138\u0137\3\2\2\2\u0139\u013a"+
		"\3\2\2\2\u013a\u0138\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013c\3\2\2\2\u013c"+
		"\u013d\b\27\2\2\u013d.\3\2\2\2\n\2\u0113\u0120\u0125\u012a\u0130\u0135"+
		"\u013a\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}