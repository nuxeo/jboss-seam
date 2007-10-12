package org.jboss.seam.wiki.core.action;

import static javax.faces.application.FacesMessage.SEVERITY_WARN;

import javax.swing.ImageIcon;

import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import org.jboss.seam.annotations.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.Component;
import org.jboss.seam.wiki.core.ui.FileMetaMap;
import org.jboss.seam.wiki.core.model.File;
import org.jboss.seam.wiki.core.model.ImageMetaInfo;
import org.jboss.seam.wiki.core.importers.metamodel.Importer;
import org.jboss.seam.wiki.core.importers.metamodel.AbstractImporter;
import org.jboss.seam.wiki.core.importers.metamodel.ImporterRegistry;
import org.jboss.seam.wiki.util.WikiUtil;

import java.util.*;

import net.sf.jmimemagic.Magic;

@Name("fileHome")
@Scope(ScopeType.CONVERSATION)
public class FileHome extends NodeHome<File> {

    public static final int PREVIEW_SIZE_MIN  = 60;
    public static final int PREVIEW_SIZE_MAX  = 1320;
    public static final int PREVIEW_ZOOM_STEP = 120;

    /* -------------------------- Context Wiring ------------------------------ */

    @In
    Map<String, FileMetaMap.FileMetaInfo> fileMetaMap;

    @In
    ImporterRegistry importerRegistry;

    /* -------------------------- Request Wiring ------------------------------ */

    /* -------------------------- Internal State ------------------------------ */

    private String filename;
    private String contentType;
    // TODO: This should really use an InputStream and directly stream into the BLOB without consuming server memory
    private byte[] filedata;
    private int imagePreviewSize = PREVIEW_SIZE_MIN;

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public byte[] getFiledata() { return filedata; }
    public void setFiledata(byte[] filedata) { this.filedata = filedata; }

    public int getImagePreviewSize() { return imagePreviewSize; }

    private Importer selectedImporter;
    public Importer getSelectedImporter() { return selectedImporter; }
    public void setSelectedImporter(Importer selectedImporter) { this.selectedImporter = selectedImporter; }

    /* -------------------------- Custom CUD ------------------------------ */

    protected boolean preparePersist() {
        // Sync file instance with form data
        syncFile();

        // Validate
        if (filedata == null || filedata.length == 0) {
            getFacesMessages().addFromResourceBundleOrDefault(
                SEVERITY_WARN,
                getMessageKeyPrefix() + "noFileUploaded",
                "Please select a file to upload"
            );
            return false;
        }

        return true;
    }

    protected boolean beforeUpdate() {

        if (selectedImporter != null) {
            AbstractImporter importer = (AbstractImporter) Component.getInstance(selectedImporter.getComponentName());
            return importer.handleImport(getEntityManager(), getInstance());
        } else {
            // Sync file instance with form data
            syncFile();

            return true;
        }
    }

    /* -------------------------- Internal Methods ------------------------------ */

    private void syncFile() {
        if (filedata != null && filedata.length >0) {
            getLog().debug("updating file data/type");

            getInstance().setFilename(filename);
            getInstance().setFilesize(filedata.length); // Don't trust the browsers headers!
            getInstance().setData(filedata);

            String mimeType;
            try {
                mimeType = Magic.getMagicMatch(filedata).getMimeType();
            } catch (Exception ex) {
                mimeType = contentType; // Default to what the browser tells us
            }
            getInstance().setContentType(mimeType);

            // Handle image/picture meta info
            if (fileMetaMap.get(getInstance().getContentType()) != null &&
                fileMetaMap.get(getInstance().getContentType()).image) {

                ImageMetaInfo imageMetaInfo =
                        getInstance().getImageMetaInfo() != null
                                ? getInstance().getImageMetaInfo()
                                : new ImageMetaInfo();
                getInstance().setImageMetaInfo(imageMetaInfo);

                ImageIcon icon = new ImageIcon(getInstance().getData());
                int imageSizeX = icon.getImage().getWidth(null);
                int imageSizeY = icon.getImage().getHeight(null);
                getInstance().getImageMetaInfo().setSizeX(imageSizeX);
                getInstance().getImageMetaInfo().setSizeY(imageSizeY);

            } else {
                getInstance().setImageMetaInfo(null);
            }
        }

        if (getInstance().getImageMetaInfo() != null && getInstance().getImageMetaInfo().getThumbnail() != 'F') {
            getLog().debug("generating thumbnail");
            int thumbnailWidth = 80;
            // TODO: We could make these sizes customizable
            switch (getInstance().getImageMetaInfo().getThumbnail()) {
                case'M':
                    thumbnailWidth = 160;
                    break;
                case'L':
                    thumbnailWidth = 320;
                    break;
            }
            getInstance().getImageMetaInfo().setThumbnailData(
                WikiUtil.resizeImage(getInstance().getData(), getInstance().getContentType(), thumbnailWidth)
            );
        }
    }

    /* -------------------------- Messages ------------------------------ */

    protected void createdMessage() {
        getFacesMessages().addFromResourceBundleOrDefault(
                SEVERITY_INFO,
                "lacewiki.msg.File.Persist",
                "File '{0}' has been saved.",
                getInstance().getName()
        );
    }

    protected void updatedMessage() {
        getFacesMessages().addFromResourceBundleOrDefault(
                SEVERITY_INFO,
                "lacewiki.msg.File.Update",
                "File '{0}' has been updated.",
                getInstance().getName()
        );
    }

    protected void deletedMessage() {
        getFacesMessages().addFromResourceBundleOrDefault(
                SEVERITY_INFO,
                "lacewiki.msg.File.Delete",
                "File '{0}' has been deleted.",
                getInstance().getName()
        );
    }

    /* -------------------------- Public Features ------------------------------ */

    public List<Importer> getAvailableImporters() {
        List<Importer> availableImporters = new ArrayList<Importer>();
        for (Importer importer : importerRegistry.getImporters()) {
            List<String> supportedMimeTypes = Arrays.asList(importer.getHandledMimeTypes());
            List<String> supportedExtensions = Arrays.asList(importer.getHandledExtensions());
            if (supportedMimeTypes.contains(getInstance().getContentType()) &&
                supportedExtensions.contains(getInstance().getExtension()) ) {
                availableImporters.add(importer);
            }
        }
        return availableImporters;
    }

    public void zoomActualSize() {
        imagePreviewSize = getInstance().getImageMetaInfo().getSizeX();
    }

    public void zoomPreviewIn() {
        if (imagePreviewSize < PREVIEW_SIZE_MAX) imagePreviewSize = imagePreviewSize + PREVIEW_ZOOM_STEP;
    }

    public void zoomPreviewOut() {
        if (imagePreviewSize > PREVIEW_SIZE_MIN && (imagePreviewSize - PREVIEW_ZOOM_STEP) > PREVIEW_SIZE_MIN)
            imagePreviewSize = imagePreviewSize - PREVIEW_ZOOM_STEP;
        else imagePreviewSize = PREVIEW_SIZE_MIN;
    }

}
