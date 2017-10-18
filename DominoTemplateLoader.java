package dpastov.freemarker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import lotus.domino.Database;
import lotus.domino.View;
import lotus.domino.Document;
import lotus.domino.NotesException;

import freemarker.cache.TemplateLoader;

public class DominoTemplateLoader implements TemplateLoader {
	private View m_view;

  // database/view with templates
	public DominoTemplateLoader(Database database) throws NotesException {
		m_view = database.getView("(TemplatesByID)");
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
		Document doc = (Document) templateSource;

		String data = null;
		
		if (doc == null) {
			data = "template was not found";
		}
		
		try {
			data = doc.getItemValueString("data"); // template as a string
		} catch (NotesException e) {
			e.printStackTrace();
		}

		return new StringReader(data);
	}
}
