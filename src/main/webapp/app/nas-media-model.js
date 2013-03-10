NasMediaApp.module('Model', function(Model, App, Backbone, Marionette, $, _) {

    Model.LocationModel = Backbone.Model.extend({
        defaults: {
            page: 'home',
            catalog: '',
            path: '',
            breadcrumbs: []
        },
        initialize: function() {
            this.on('change:catalog', this._resetPath);
        },
        buildHref: function() {
            return this._buildPath();
        },
        buildUrl: function() {
            return this._buildPath('repo/');
        },
        prepareBreadcrumbs: function() {
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
                            var crumb = this._createBreadcrumb(parts[i], currentHref, currentPath);
                            breadcrumbs.push(crumb);
                        }
                    }
                }
            }
            //Store breadcrumbs
            this.set({breadcrumbs: breadcrumbs}, {silent: true});
        },
        _createBreadcrumb: function(name, href, path) {
            return {name: name, href: href, path: path};
        },
        _buildPath: function(prefix) {
            prefix = prefix || '';
            var path = prefix + this.get('page');
            if (this.has('catalog')) {
                path += '/' + this.get('catalog');
                if (this.has('path')) {
                    path += '/' + this.get('path');
                }
            }
            return path;
        },
        _resetPath: function() {
            this.set({'path': ''});
        }
    });

});