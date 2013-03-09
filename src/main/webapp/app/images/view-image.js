NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {

    Views.ImagesPageLayout = Marionette.Layout.extend({
        template: '#template-image-page-layout',
        regions: {
            sidebar: '#sidebar',
            breadcrumbs: '#breadcrumbs',
            content: '#content'
        },
        initialize: function() {
            this.catalogsView = new Views.CatalogsView({
                itemView: App.Views.ImageCatalogView,
                collection: this.model.catalogs
            });
            this.breadcrumbsView = new Views.BreadcrumbsView({
                model: this.model
            });
            this.contentView = new Views.ImagesLayout({
                model: this.model
            });
        },
        onRender: function() {
            this.breadcrumbs.show(this.breadcrumbsView);
            this.sidebar.show(this.catalogsView);
            this.content.show(this.contentView);
        }
    });

    Views.ImagesLayout = Marionette.Layout.extend({
        template: '#template-image-content-layout',
        regions: {
            folders: '#folders',
            files: '#files'
        },
        initialize: function() {
            this.listenTo(this.model, 'change:path', this.render);
        },
        onRender: function() {
            var self = this;
            this.model.mediaCollection = new App.Catalogs.NodeCollection();
            this.model.mediaCollection.url = this.model.buildUrl();
            this.model.mediaCollection.fetch({success: function(collection) {
                    //Generate folders view
                    var folders = collection.filter(function(node) {
                        return node.get('@type') === 'Folder';
                    });
                    var foldersViews = new Views.FoldersView({
                        collection: new App.Catalogs.FolderCollection(folders)
                    });
                    self.folders.show(foldersViews);

                    //Generate images view
                    var files = collection.filter(function(node) {
                        return node.get('@type') === 'Image';
                    });
                    var filesView = new Views.ImagesView({
                        collection: new App.Images.ImageCatalogCollection(files)
                    });
                    self.files.show(filesView);
                }});
        }
    });

    Views.ImageView = Marionette.ItemView.extend({
        tagName: 'li',
        className: 'span2',
        template: '#template-image'
    });

    Views.ImagesView = Marionette.CollectionView.extend({
        tagName: 'ul',
        className: 'thumbnails',
        itemView: Views.ImageView,
    });
});

