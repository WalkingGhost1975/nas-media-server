NasMediaApp.module('Model', function(Model, App, Backbone, Marionette, $, _){

  Model.MediaModel = Backbone.Model.extend({

    defaults: {
        page : 'home',
        catalog : '',
        path : '',
        breadcrumbs : []
    },

    initialize : function() {
        this.on('change:catalog', this._resetPath);
        this.on('change:catalog', this._createBreadcrumbs);
        this.on('change:path', this._createBreadcrumbs);
    },

    buildUrl : function()  {
        var url = 'repo/' + this.get('page');
        if (this.has('catalog'))  {
            url += '/' + this.get('catalog');
            if (this.has('path'))  {
                url += '/' + this.get('path');
            }
        }
        return url;
    },

    getSelectedCatalog  : function()  {
        var selectedCatalog = this.catalogs.find(function(catalog) {
                return catalog.get('active');
            });
        return selectedCatalog;
    },

    syncToCatalogCollection  : function()  {
        var self = this;
        var activeCatalog = this.catalogs.find(function(catalog) {
            return catalog.get('name') === self.get('catalog');
        });
        if (activeCatalog) {
            activeCatalog.set({active : true});
        }
    },

    syncFromCatalogCollection  : function()  {
        this.set({catalog : this.getSelectedCatalog().get('name')});
    },

    _createBreadcrumbs : function()  {
        var breadcrumbs = [];
        if (this.has('catalog')) {
            var catalogCrumb = this._createBreadcrumb(this.get('catalog'), this.get('page') + '/' + this.get('catalog'), '/');
            breadcrumbs = [catalogCrumb];
            var currentPath = '';
            var currentHref = this.get('page') + '/' + this.get('catalog');
            if (this.has('path')) {
                var path = this.get('path');
                if (path.length > 0) {
                    path = (path.charAt(0) === '/') ? path.substr(1) : path;
                    var parts = path.split('/');
                    for (var i = 0; i < parts.length; i++) {
                        currentPath += '/' + parts[i];
                        currentHref += '/' + parts[i];
                        var crumb = this._createBreadcrumb(parts[i],currentHref,currentPath);
                        breadcrumbs.push(crumb);
                    }
                }
            }
        }
        this.set({'breadcrumbs' : breadcrumbs});
    },

    _resetPath : function() {
        this.set({'path' : ''});
    },

    _createBreadcrumb : function(name, href, path)  {
        return {name : name, href : href, path : path};
    },
    catalogs : new Backbone.Collection()
  });

});