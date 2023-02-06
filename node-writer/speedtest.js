const { BigQuery } = require('@google-cloud/bigquery');

const projectId = 'iry-cloud-101'; // Replace with your project ID
const datasetId = 'speedtest';
const tableId = 'test_results';

// Code continues here

async function writeEventToBQ(receivedEvent) {
    const event = {...receivedEvent, timestamp: new Date(receivedEvent.timestamp)};

    const bigquery = new BigQuery({
        projectId: projectId,
    });

    console.log('About to insert: ', event);

    await bigquery
        .dataset(datasetId)
        .table(tableId)
        .insert([event])
        .then(() => {
            console.log(`Inserted the event`);
        })
        .catch(err => {
            if (err && err.name === 'PartialFailureError') {
                if (err.errors && err.errors.length > 0) {
                    console.log('Insert errors:');
                    err.errors.forEach(err => console.error(err));
                }
            } else {
                console.error('ERROR:', err);
            }
        });
};

exports.handleSpeedtestEvent = async (speedtestEvent) => {
    await writeEventToBQ(speedtestEvent);
};