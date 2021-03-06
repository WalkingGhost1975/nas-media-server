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
            this.model.setActive();
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
        },
        onBeforeRender: function() {
            this.collection.syncActiveCatalog();
        }
    });

    Views.BreadcrumbsView = Marionette.ItemView.extend({
        tagName: 'ul',
        className: 'breadcrumb',
        template: '#template-breadcrumbs',
        initialize: function() {
            this.listenTo(this.model,'change',this.render);
        },
        events: {
            'click a': 'selectPath'
        },
        onBeforeRender : function() {
            this.model.prepareBreadcrumbs();
        },
        selectPath: function(evt) {
            var path = $(evt.currentTarget).data('path').substr(1);
            this.model.set('path', path);
        }
    });

    Views.FolderView = Marionette.ItemView.extend({
        tagName: 'li',
        template: '#template-folder',
        events: {
            'click a': 'openFolder'
        },
        openFolder : function()  {
            this.model.openFolder();
        }
    });

    Views.FoldersView = Marionette.CollectionView.extend({
        tagName: 'ul',
        className: 'thumbnails',
        itemView: Views.FolderView,
        initialize: function() {
        }
    });
});
