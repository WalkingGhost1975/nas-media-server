NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {

    Views.AudioPageLayout = Marionette.Layout.extend({
        template: '#template-audio-page-layout',
        regions: {
            sidebar: '#sidebar',
            content: '#content'
        },
        initialize: function() {
            this.catalogs = new App.Audio.AudioCatalogCollection([],{location : this.model});
        },
        onRender: function() {
            //Prepare view
            var catalogsView = new App.Views.CatalogsView({
                itemView: App.Views.AudioCatalogView,
                collection: this.catalogs
            });
            this.sidebar.show(catalogsView);
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

