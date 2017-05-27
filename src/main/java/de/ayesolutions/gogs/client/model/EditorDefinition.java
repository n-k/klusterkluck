package de.ayesolutions.gogs.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Christian Aye - c.aye@aye-solutions.de
 */
public class EditorDefinition {

    private String selector;

    private String charset;

    @JsonProperty("indent_style")
    private String indentStyle;

    @JsonProperty("indent_size")
    private String indentSize;

    @JsonProperty("tab_width")
    private int tabWidth;

    @JsonProperty("end_of_line")
    private String endOfLine;

    @JsonProperty("trim_trailing_whitespace")
    private boolean trimTrailingWhitespace;

    @JsonProperty("insert_final_newline")
    private boolean insertFinalNewLine;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getIndentStyle() {
        return indentStyle;
    }

    public void setIndentStyle(String indentStyle) {
        this.indentStyle = indentStyle;
    }

    public String getIndentSize() {
        return indentSize;
    }

    public void setIndentSize(String indentSize) {
        this.indentSize = indentSize;
    }

    public int getTabWidth() {
        return tabWidth;
    }

    public void setTabWidth(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    public String getEndOfLine() {
        return endOfLine;
    }

    public void setEndOfLine(String endOfLine) {
        this.endOfLine = endOfLine;
    }

    public boolean isTrimTrailingWhitespace() {
        return trimTrailingWhitespace;
    }

    public void setTrimTrailingWhitespace(boolean trimTrailingWhitespace) {
        this.trimTrailingWhitespace = trimTrailingWhitespace;
    }

    public boolean isInsertFinalNewLine() {
        return insertFinalNewLine;
    }

    public void setInsertFinalNewLine(boolean insertFinalNewLine) {
        this.insertFinalNewLine = insertFinalNewLine;
    }
}
