NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {

    Views.AudioPageLayout = Marionette.Layout.extend({
        template: '#template-audio-page-layout',
        regions: {
            sidebar: '#sidebar',
            breadcrumbs: '#breadcrumbs',
            content: '#content'
        },
        initialize: function() {
            //Prepare view
            this.catalogsView = new App.Views.CatalogsView({
                itemView: App.Views.AudioCatalogView,
                collection: this.model.catalogs
            });
            this.breadcrumbsView = new App.Views.BreadcrumbsView({
                model: this.model
            });
        },
        onRender: function() {
            this.sidebar.show(this.catalogsView);
            this.breadcrumbs.show(this.breadcrumbsView);
        }
    });


    Views.AudioFileView = Marionette.ItemView.extend({
        tagName: 'tr',
        template: '#template-audio-file'
    });

    Views.AudioFilesView = Marionette.CollectionView.extend({
        tagName: '',
        itemView: Views.AudioFileView,
        itemViewContainer: '#content'
    });

});

