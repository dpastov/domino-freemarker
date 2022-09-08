package dpastov.freemarker;

import java.io.IOException;
import java.io.Reader;
import lotus.domino.Database;
import lotus.domino.View;
import lotus.domino.Document;
import lotus.domino.NotesException;

import freemarker.cache.TemplateLoader;

public class DominoTemplateLoader implements TemplateLoader {
	private View m_view;

	public DominoTemplateLoader(Database database) throws NotesException {
		m_view = database.getView("($Template)");
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		Document doc = (Document) templateSource;
		try {
			doc.recycle();
		} catch (NotesException e) {
			e.printStackTrace();
		}
	}

	public Object findTemplateSource(String id) throws IOException {
		try {
			return m_view.getDocumentByKey(id, true);
		} catch (NotesException e) {
			e.printStackTrace();
		}

		return null;
	}

	public long getLastModified(Object templateSource) {
		Document doc = (Document) templateSource;

		try {
			return doc.getLastModified().toJavaDate().getTime();
		} catch (NotesException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		if (templateSource == null) return null;
		
		Document doc = (Document) templateSource;
		try {
			return doc.getFirstItem("Body").getReader();
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return null;
	}
}
