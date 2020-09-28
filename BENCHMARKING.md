# Benchmark Subscription Matcher

Matcher comes with built-in benchmarking mechanism that makes use of the
OptaPlanner Benchmarker.


## Why

Running benchmark is helpful when:
- fine-tuning OptaPlanner parameters,
- comparing two (or more) OptaPlanner configurations.


## How

Use the `benchmarking/benchmark.sh` helper script.

You must pass the OptaPlanner benchmarker configuration file as the first
argument. See `benchmarking/config.example.xml` for an example.

You also need to pass one or more input files. `input.json` from the test data
can be used, but mostly you'll use some real-life data.

After the run, the browser is opened and you can examined the results.

### Example

```bash
./benchmark.sh config.xml input-user-1.json input-user-2.json
```

