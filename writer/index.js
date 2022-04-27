const speedtest = require('./speedtest');

exports.processSpeedtestEvent = async (pubSubMessage) => {
    if (pubSubMessage.data) {
        const speedtestEvent = Buffer.from(pubSubMessage.data, 'base64').toString();
        await speedtest.handleSpeedtestEvent(JSON.parse(speedtestEvent));
    }
};