function Properties() {
    //	this.staticUrl = '/portfolio/';
    this.staticUrl = '/mycvs/';
    this.baseUrl = 'https://' + window.document.location.hostname + (window.document.location.port == '' ? '' : (':' + window.document.location.port)) + this.staticUrl;
    this.logoutUrl = 'https://' + window.document.location.hostname + (window.document.location.port == '' ? '' : (':' + window.document.location.port)) + this.staticUrl + 'logout';

    this.showmsec = 0;
    this.hidemsec = 0;

    this.studentInfos = ['cds', 'enrollmentYear', 'academicYear',
        'supplementaryYears', 'cfu_cfuTotal', 'marksNumber',
        'marksAverage', 'gender', 'dateOfBirth', 'nation', 'address',
        'email', 'phone', 'mobile'
    ];

    this.categories = {
    	'language': {
    		choose: ['language']
    	},
    	'skill': {
    		choose: ['skill']
    	},
    	'contact': {
    		choose: ['contact']
    	},
        'education': {
            containerSelector: '#section_education',
            choose: ['simple']
        },
        'presentation': {
            containerSelector: '#section_presentation',
            choose: ['raw', 'video'],
            tools: ['eye']
        },
        'professional': {
            containerSelector: '#section_professional',
            choose: ['simple_desc']
        },
        'about': {
            containerSelector: '#section_about',
            choose: ['simple_pic']
        },
        'cherryotc': {
            containerSelector: '#section_cherryotc',
            choose: []
        }
    };

    this.toolsDefaultStudentInfos = ['eye'];
    this.toolsDefaultCategories = ['eye', 'cherry'];

    this.entries = {
        'user_pic': {
            tools: ['lax']
        },
        'language': {
            tools: ['lax']
        },
        'skill': {
            tools: ['lax']
        },
        'contact': {
            tools: ['lax']
        },
        'raw': {
            description: 'Text',
            tools: ['edit', 'delete', 'share']
        },
        'simple': {
            tools: ['edit', 'delete', 'share']
        },
        'simple_desc': {
            tools: ['edit', 'delete', 'share']
        },
        'simple_pic': {
            tools: ['edit', 'delete', 'share']
        },
        'video': {
            description: 'Video',
            tools: ['edit', 'delete', 'share'],
            maxWidth: 390,
            maxHeight: 293
        },
        'sys_simple': {
            tools: ['share']
        }
    };

    this.SYS_entry_dialog = 'Having enabled this entry, if you share this CV, your University marks will be visible to other users.';
};
