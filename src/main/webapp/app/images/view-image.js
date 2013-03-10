NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {

    Views.ImagesPageLayout = Marionette.Layout.extend({
        template: '#template-image-page-layout',
        regions: {
            sidebar: '#sidebar',
            content: '#content'
        },
        initialize: function() {
            this.catalogs = new App.Images.ImageCatalogCollection([],{location : this.model});
        },
        onRender: function() {
            var catalogsView = new Views.CatalogsView({
                itemView: App.Views.ImageCatalogView,
                collection: this.catalogs
            });
            this.sidebar.show(catalogsView);

            var contentView = new Views.ImagesLayout({
                model: this.model
            });
            this.content.show(contentView);
        }
    });

    Views.ImagesLayout = Marionette.Layout.extend({
        template: '#template-image-content-layout',
        regions: {
            breadcrumbs: '#breadcrumbs',
            folders: '#folders',
            files: '#files'
        },
        initialize: function() {
            this.listenTo(this.model, 'change', this.render);
        },
        onRender: function() {
            //Only render view a catalog is selected
            if (this.model.has('catalog')) {
                var breadcrumbsView = new Views.BreadcrumbsView({
                    model: this.model
                });
                this.breadcrumbs.show(breadcrumbsView);

                var self = this;
                this.nodes = new App.Catalogs.NodeCollection();
                this.nodes.url = this.model.buildUrl();
                App.vent.trigger('display:overlay');
                this.nodes.fetch({success: function() {
                        //Generate folders view
                        var foldersViews = new Views.FoldersView({
                            collection: self.nodes.getFolders({location: self.model})
                        });
                        self.folders.show(foldersViews);

                        //Generate images view
                        var files = new App.Images.ImageCatalogCollection([],{location: self.model});
                        self.nodes.getMediaFiles(files, {location: self.model});
                        var filesView = new Views.ImagesView({
                            collection: files
                        });
                        self.files.show(filesView);
                        App.vent.trigger('hide:overlay');
                    }});
            }
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
        itemView: Views.ImageView
    });
});

