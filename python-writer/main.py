
import json
import base64
import datetime
from google.cloud import bigquery

# Project ID
project_id = "sfl-cloud-101" # Your project id

# bq client
bq_client = bigquery.Client(project=project_id)
table_id = f"{project_id}.speedtest.test_results"

# Get message in main
def main(event, context):
    """Triggered from a message on the speedtest Cloud Pub/Sub topic.
    Args:
        event (dict): Event payload.
        context (google.cloud.functions.Context): Metadata for the event.
    """

    # Decode from bytearray
    pubsub_message = json.loads(base64.b64decode(event['data']).decode('utf-8'))

    # Convert timestamp to ISO format
    pubsub_message['timestamp'] = datetime.datetime.fromtimestamp(int(pubsub_message['timestamp'])).isoformat()

    # Insert to bq
    errors = bq_client.insert_rows_json(table_id, [pubsub_message])

    if errors != []:
        print("Message processing failed. Error:", errors)
    else:
        print('Message processed.')