using namespace std;

public class ParentWidget {};
public class Pattern {};
public class StringBuffer {};

class BoldWidget : public ParentWidget {
  public:
    static const string REGEXP;
    static const Pattern& pattern;
    ParentWidget* parent;
    BoldWidget(ParentWidget& parent, string text);
    string render();
};
const Pattern& BoldWidget::pattern = Pattern::compile("'''(.+?)'''",
      Pattern::MULTILINE + Pattern::DOTALL
);
const string BoldWidget::REGEXP = "'''.+?'''";
BoldWidget::BoldWidget(ParentWidget& parent, string text) {
  Matcher* match = pattern.matcher(text);
  match -> find();
  addChildWidgets(match -> group(1));
}
string BoldWidget::render() {
  StringBuffer *html = new StringBuffer("<br>");
  html -> append(childHtml()).append("<br>");
  return html -> toString();
}
