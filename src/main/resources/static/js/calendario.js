const { useState, useEffect } = React;

function CalendarioFiltrato({ torneoId }) {

    const [filtro, setFiltro] = useState("TUTTE");
    const [partite, setPartite] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(function() {
        fetch('/api/torneo/' + torneoId + '/calendario')
            .then(function(res) { return res.json(); })
            .then(function(data) {
                setPartite(data);
                setLoading(false);
            });
    }, []);

    const partiteFiltrate = (filtro === "TUTTE")
        ? partite
        : partite.filter(function(p) { return p.stato === filtro; });

    if (loading) {
        return React.createElement('p', { className: 'text-muted' }, 'Caricamento...');
    }

    const bottoni = React.createElement('div',
        { style: { marginBottom: '20px', display: 'flex', gap: '10px', flexWrap: 'wrap' } },
        React.createElement('button', {
            className: 'btn' + (filtro === 'TUTTE'     ? ' btn-primary' : ''),
            onClick: function() { setFiltro('TUTTE'); }
        }, 'Tutte'),
        React.createElement('button', {
            className: 'btn' + (filtro === 'SCHEDULED' ? ' btn-primary' : ''),
            onClick: function() { setFiltro('SCHEDULED'); }
        }, 'Da giocare'),
        React.createElement('button', {
            className: 'btn' + (filtro === 'PLAYED'    ? ' btn-primary' : ''),
            onClick: function() { setFiltro('PLAYED'); }
        }, 'Già giocate')
    );

    if (partiteFiltrate.length === 0) {
        return React.createElement('div', null,
            bottoni,
            React.createElement('p', { className: 'empty-state' },
                'Nessuna partita in questa categoria.')
        );
    }

    const righe = partiteFiltrate.map(function(p) {

        const data = p.dataOra
            ? new Date(p.dataOra).toLocaleString('it-IT', {
                day: '2-digit', month: '2-digit', year: 'numeric',
                hour: '2-digit', minute: '2-digit'
              })
            : '';

        const risultato = (p.stato === 'PLAYED')
            ? React.createElement('span', { style: { margin: '0 10px', fontWeight: '700' } },
                  p.goalsHome + ' : ' + p.goalsAway)
            : React.createElement('span', { style: { margin: '0 10px', color: '#666' } }, 'vs');

        const contenuto = React.createElement('div', null,
            React.createElement('span', { className: 'text-muted' }, data),
            React.createElement('br'),
            React.createElement('strong', null, p.squadraHome),
            risultato,
            React.createElement('strong', null, p.squadraAway)
        );

        const linkDettaglio = React.createElement('a', {
            href: '/partita/' + p.id,
            className: 'btn btn-primary'
        }, 'Dettagli');

        return React.createElement('li', { key: p.id, className: 'list-item' },
            contenuto,
            linkDettaglio
        );
    });

    const lista = React.createElement('ul', { className: 'list-group' }, righe);

    return React.createElement('div', null, bottoni, lista);
}

const container = document.getElementById('calendario-react');
const torneoId  = container.getAttribute('data-torneo-id');
const root      = ReactDOM.createRoot(container);
root.render(React.createElement(CalendarioFiltrato, { torneoId: torneoId }));