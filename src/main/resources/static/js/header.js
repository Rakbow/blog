var navbarItems = [
    {
        label: 'Album',
        icon: 'pi pi-fw pi-list',
        items: [
            {
                label: 'Album List',
                icon: 'pi pi-fw pi-list',
                url: 'http://localhost:8080/db/album-list'
            },
            {
                label: 'Album Card',
                icon: 'pi pi-fw pi-list',
                url: 'http://localhost:8080/db/album-card'
            }
        ]
    },
    {
        label: 'Disc',
        icon: 'pi pi-fw pi-video',
        items: [
            {
                label: 'Develop',
                icon: 'pi pi-fw pi-wrench'
            },
            {
                label: 'Develop',
                icon: 'pi pi-fw pi-wrench'
            }
        ]
    },
    {
        label: 'Book',
        icon: 'pi pi-fw pi-book',
        items: [
            {
                label: 'Book List',
                icon: 'pi pi-fw pi-book'
            },
            {
                label: 'Develop',
                icon: 'pi pi-fw pi-wrench'
            }
        ]
    },
    {
        label: 'Users',
        icon: 'pi pi-fw pi-user',
        items: [
            {
                label: 'New',
                icon: 'pi pi-fw pi-user-plus',
            },
            {
                label: 'Delete',
                icon: 'pi pi-fw pi-user-minus',
            },
            {
                label: 'Search',
                icon: 'pi pi-fw pi-users',
                items: [
                    {
                        label: 'Filter',
                        icon: 'pi pi-fw pi-filter',
                        items: [
                            {
                                label: 'Print',
                                icon: 'pi pi-fw pi-print'
                            }
                        ]
                    },
                    {
                        icon: 'pi pi-fw pi-bars',
                        label: 'List'
                    }
                ]
            }
        ]
    },
    {
        label: 'Quit',
        icon: 'pi pi-fw pi-power-off'
    }
];