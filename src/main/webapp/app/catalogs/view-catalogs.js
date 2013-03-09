NasMediaApp.module('Views', function(Views, App, Backbone, Marionette, $, _) {
    Views.CatalogView = Marionette.ItemView.extend({
        tagName: 'li',
        template: '#template-catalog',
        ui: {
            item: 'i',
            link: 'a'
        },
        events: {
            'click a': 'selectCatalog'
        },
        onRender: function() {
            var className = this.model.get('active') ? 'icon-folder-open' : 'icon-folder-close';
            this.ui.item.addClass(className);
        },
        selectCatalog: function() {
            this.model.set({active: true});
        }
    });

    Views.CatalogsView = Marionette.CollectionView.extend({
        tagName: 'ul',
        className: 'unstyled',
        itemView: Views.CatalogView,
        initialize: function() {
            this.collection.fetch();
        },
        collectionEvents: {
            'change:active': 'render'
        }
    });
});
