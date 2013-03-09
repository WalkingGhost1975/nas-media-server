NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {

    Views.ImagesLayout = Marionette.Layout.extend({
        template: '#template-image-view',
        regions: {
            sidebar: '#sidebar',
            breadcrumbs: '#breadcrumbs',
            content: '#content'
        },
        initialize: function() {
            this.contentView = new App.Views.ImagesView();

            this.catalogsView = new App.Views.CatalogsView({
                itemView: App.Views.ImageCatalogView,
                collection: this.model.catalogs
            });
            this.breadcrumbsView = new App.Views.BreadcrumbsView({
                model: this.model
            });
        },
        onRender: function() {
            this.content.show(this.contentView);
            this.breadcrumbs.show(this.breadcrumbsView);
            this.sidebar.show(this.catalogsView);
        }
    });

    Views.ImageView = Marionette.ItemView.extend({
        tagName: 'li',
        className: 'span2',
        template: '#template-image',
    });

    Views.ImagesView = Marionette.CollectionView.extend({
        tagName: 'ul',
        className: 'thumbnails',
        itemView: Views.ImageView,
    });
});

